package tech.deplant.java4ever.binding.ffi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import tech.deplant.java4ever.binding.*;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * The type Ever sdk context.
 */
public class EverSdkContext implements tc_response_handler_t.Function {

	private final static System.Logger logger = System.getLogger(EverSdkContext.class.getName());

	private final int id;
	private final AtomicInteger requestCount = new AtomicInteger();
	private final Client.ClientConfig clientConfig;
	private final long timeout;
	@JsonIgnore private final Map<Integer, RequestData> requests = new ConcurrentHashMap<>();
	@JsonIgnore private final Queue<RequestData> cleanupQueue = new ConcurrentLinkedDeque<>();

	/**
	 * Instantiates a new Ever sdk context.
	 *
	 * @param id           the id
	 * @param clientConfig the client config
	 */
	public EverSdkContext(int id, Client.ClientConfig clientConfig) {
		this.id = id;
		this.clientConfig = clientConfig;
		this.timeout = extractTimeout(clientConfig);
	}

	/**
	 * Most used call to EVER-SDK with some output object
	 *
	 * @param <R>            Class of the result object
	 * @param <P>            Class of the function params object
	 * @param <AP>           Class of the AppObject param calls
	 * @param <AR>           Class of the AppObject result callbacks
	 * @param functionName   the function name
	 * @param functionInputs record of input type, usually ParamsOf...
	 * @param resultClass    class of output type record, usually ResultOf...class
	 * @param eventConsumer  the event consumer
	 * @param appObject      the app object
	 * @return output type record, usually ResultOf...
	 * @throws EverSdkException the ever sdk exception
	 */
	public <R, P, AP, AR> CompletableFuture<R> callAsync(final String functionName,
	                                                     final P functionInputs,
	                                                     final Class<R> resultClass,
	                                                     final Consumer<JsonNode> eventConsumer,
	                                                     final AppObject appObject) {
		final int requestId = requestCountNextVal();
		// let's clean previous requests and their native memory sessions
		cleanup();

		// it's better to explicitly mark requests as void to not recheck this every time in response handler
		boolean hasResponse = !resultClass.equals(Void.class);

		var request = new RequestData<R>(hasResponse,
		                                 Arena.ofAuto(),
		                                 new ReentrantLock(),
		                                 resultClass,
		                                 new CompletableFuture<>(),
		                                 eventConsumer,
		                                 appObject);
		this.requests.put(requestId, request);

		// with reentrant lock, multiple results on any given single request will be processed one by one
		var paramsJson = processParams(functionInputs);
		request.queueLock().lock();
		try {
			NativeMethods.tcRequest(this.id, functionName, paramsJson, request.nativeArena(), requestId, this);
		} finally {
			request.queueLock().unlock();
			logger.log(System.Logger.Level.TRACE,
			           () -> EverSdk.LOG_FORMAT.formatted(this.id, requestId, functionName, "SEND", paramsJson));
		}
		if (!hasResponse) {
			request.responseFuture().complete(null);
		}
		return request.responseFuture();
	}

	/**
	 * Request count next val int.
	 *
	 * @return the int
	 */
	public int requestCountNextVal() {
		return this.requestCount.incrementAndGet();
	}

	/**
	 * Add response.
	 *
	 * @param requestId      the request id
	 * @param responseString the response string
	 */
	public void addResponse(int requestId, final RequestData request, final String responseString) {
		if (request.hasResponse()) {
			try {
				if (!request.responseFuture().isDone()) {
					logger.log(System.Logger.Level.TRACE,
					           () -> "CTX:%d REQ:%d RESP:%s".formatted(this.id, requestId, responseString));
					request.responseFuture()
					       .complete(JsonContext.SDK_JSON_MAPPER().readValue(responseString, request.responseClass()));
				} else {
					logger.log(System.Logger.Level.ERROR,
					           "Slot for this request not found on processing response! CTX:%d REQ:%d RESP:%s".formatted(
							           this.id,
							           requestId,
							           responseString));
				}
			} catch (JsonProcessingException ex2) {
				// successful response but parsing failed
				logger.log(System.Logger.Level.ERROR,
				           () -> "CTX:%d REQ:%d EVER-SDK Response deserialization failed! %s".formatted(this.id,
				                                                                                        requestId,
				                                                                                        ex2.toString()));
				request.responseFuture()
				       .completeExceptionally(new EverSdkException(new EverSdkException.ErrorResult(-500,
				                                                                                    "EVER-SDK response deserialization failed!"),
				                                                   ex2.getCause()));
			}
		}
	}

	private void addError(int requestId, final RequestData request, final String responseString) {
		if (request.responseFuture() instanceof CompletableFuture<?> future) {
			try {
				// These errors are sent by SDK, response_type=1
				//String everSdkError = ex.getCause().getMessage();
				logger.log(System.Logger.Level.WARNING,
				           () -> "CTX:%d REQ:%d ERR:%s".formatted(this.id, requestId, responseString));
				// let's try to parse error response
				EverSdkException.ErrorResult sdkResponse = JsonContext.SDK_JSON_MAPPER()
				                                                      .readValue(responseString,
				                                                                 EverSdkException.ErrorResult.class);
				future.completeExceptionally(new EverSdkException(sdkResponse));
			} catch (JsonProcessingException ex1) {
				// if error response parsing failed
				logger.log(System.Logger.Level.ERROR,
				           () -> "CTX:%d REQ:%d EVER-SDK Error deserialization failed! %s".formatted(this.id,
				                                                                                     requestId,
				                                                                                     ex1.toString()));
				future.completeExceptionally(new EverSdkException(new EverSdkException.ErrorResult(-500,
				                                                                                   "EVER-SDK Error deserialization failed!"),
				                                                  ex1.getCause()));
			}
		} else {
			logger.log(System.Logger.Level.ERROR,
			           "Slot for this request not found on processing error response! CTX:%d REQ:%d ERR:%s".formatted(
					           this.id,
					           requestId,
					           responseString));
		}

	}

	// responseType = 100 means good answer, 101 means error or reconnection
	private void addAppObjectRequest(int requestId, final RequestData request, final String appRequestString) {
		if (request.appObject() != null) {
			try {
				var appRequest = JsonContext.SDK_JSON_MAPPER()
				                            .readValue(appRequestString, Client.ParamsOfAppRequest.class);
				try {
					request.appObject().consumeParams(this.id, appRequest.appRequestId(), appRequest.requestData());
				} catch (Exception ex1) {
					logger.log(System.Logger.Level.ERROR,
					           () -> "REQ:%d EVENT:%s AppRequest processing failed! %s".formatted(requestId,
					                                                                              appRequestString,
					                                                                              ex1.toString()));
				}
			} catch (JsonProcessingException ex2) {
				logger.log(System.Logger.Level.ERROR,
				           () -> "REQ:%d EVENT:%s AppRequest JSON deserialization failed! %s".formatted(requestId,
				                                                                                        appRequestString,
				                                                                                        ex2.toString()));
			}
		} else {
			logger.log(System.Logger.Level.ERROR,
			           "No app request consumer for this request_id! CTX:%d REQ:%d EVENT:%s".formatted(this.id,
			                                                                                           requestId,
			                                                                                           appRequestString));
		}
	}

	// responseType = 100 means good answer, 101 means error or reconnection
	private void addEvent(int requestId, final RequestData request, final String responseString, int responseType) {
		if (request.subscriptionHandler() != null) {
			try {
				JsonNode node = JsonContext.ABI_JSON_MAPPER().readTree(responseString);
				try {
					request.subscriptionHandler().accept(node);
				} catch (Exception ex1) {
					logger.log(System.Logger.Level.ERROR,
					           () -> "REQ:%d EVENT:%s Subscribe Event Action processing failed! %s".formatted(requestId,
					                                                                                          responseString,
					                                                                                          ex1.toString()));
				}
			} catch (JsonProcessingException ex2) {
				logger.log(System.Logger.Level.ERROR,
				           () -> "REQ:%d EVENT:%s Subscribe Event JSON deserialization failed! %s".formatted(requestId,
				                                                                                             responseString,
				                                                                                             ex2.toString()));
			}
		} else {
			logger.log(System.Logger.Level.ERROR,
			           "No event consumer for this request_id! CTX:%d REQ:%d EVENT:%s".formatted(this.id,
			                                                                                     requestId,
			                                                                                     responseString));
		}
	}

	private void finishRequest(int requestId, final RequestData request) {
		this.cleanupQueue.add(request);
		this.requests.remove(requestId);
	}

	private void cleanup() {
		while (this.cleanupQueue.poll() instanceof RequestData request) {
			request.queueLock().lock();
			//try (var arena = request.nativeArena()) {
			logger.log(System.Logger.Level.TRACE, () -> "Memory session arena closed");
			//} finally {
			request.queueLock().unlock();
			//}
		}
	}

	private <P> String processParams(final P params) {
		try {
			return (null == params) ? "" : JsonContext.SDK_JSON_MAPPER().writeValueAsString(params);
		} catch (JsonProcessingException e) {
			logger.log(System.Logger.Level.ERROR,
			           () -> "Parameters serialization failed!" + e.getMessage() + e.getCause());
			throw new IllegalArgumentException("Parameters serialization failed!", e);
		}
	}

	private long extractTimeout(Client.ClientConfig cfg) {
		return switch (cfg) {
			case null -> 60000L;
			case Client.ClientConfig cl -> switch (cl.network()) {
				case null -> 60000L;
				case Client.NetworkConfig ntwrk -> Objects.requireNonNullElse(ntwrk.queryTimeout(), 60000L);
			};
		};
	}

	/**
	 * Id int.
	 *
	 * @return the int
	 */
	public int id() {
		return this.id;
	}

	/**
	 * Request count int.
	 *
	 * @return the int
	 */
	public int requestCount() {
		return this.requestCount.get();
	}

	/**
	 * Config client . client config.
	 *
	 * @return the client . client config
	 */
	public Client.ClientConfig config() {
		return this.clientConfig;
	}

	/**
	 * @param request_id    id of the request in this context that this answer applies to
	 * @param params_json   memory segment with native response string
	 * @param response_type Type of response with following possible values:
	 *                      RESULT = 1, real response.
	 *                      NOP = 2, no operation. In combination with finished = true signals that the request handling was finished.
	 *                      APP_REQUEST = 3, request some data from application. See Application objects
	 *                      APP_NOTIFY = 4, notify application with some data. See Application objects
	 *                      RESERVED = 5..99 – reserved for protocol internal purposes. Application (or binding) must ignore this response.
	 *                      CUSTOM >= 100 - additional function data related to request handling. Depends on the function.
	 * @param finished      is this a final answer for this request_id
	 */
	@Override
	public void apply(int request_id, final MemorySegment params_json, int response_type, boolean finished) {
		final String responseString = NativeStrings.toJava(params_json);
		if (logger.isLoggable(System.Logger.Level.TRACE)) {
			logger.log(System.Logger.Level.TRACE,
			           "CTX:%d, REQ:%d TYPE:%d FINISHED:%s JSON:%s".formatted(this.id,
			                                                                  request_id,
			                                                                  response_type,
			                                                                  String.valueOf(finished),
			                                                                  responseString));
		}
		if (this.requests.get(request_id) instanceof RequestData<?> request) {
			// Request is present, let's lock it
			request.queueLock().lock();
			try {
				switch (tc_response_types.of(response_type)) {
					case tc_response_types.TC_RESPONSE_SUCCESS -> addResponse(request_id, request, responseString);
					case tc_response_types.TC_RESPONSE_ERROR -> addError(request_id, request, responseString);
					case tc_response_types.TC_RESPONSE_CUSTOM ->
							addEvent(request_id, request, responseString, response_type);
					case tc_response_types.TC_RESPONSE_APP_REQUEST ->
							addAppObjectRequest(request_id, request, responseString);
				}
				// if "finished" boolean flag received, let's cleanup request
				if (finished) {
					finishRequest(request_id, request);
				}
			} catch (Exception e) {
				logger.log(System.Logger.Level.ERROR,
				           "REQ:%d TYPE:%d EVER-SDK Unexpected upcall error! %s".formatted(request_id,
				                                                                           response_type,
				                                                                           e.toString()));
			} finally {
				request.queueLock().unlock();
			}
		} else {
			// Let's process the situation when request already cleaned up
			logger.log(System.Logger.Level.ERROR,
			           "REQ:%d TYPE:%d EVER-SDK Response on cleaned request!".formatted(request_id, response_type));
		}
	}

	/**
	 * The type Request data.
	 * It's VERY IMPORTANT to hold the pointer to RequestData for all interconnection of EVER-SDK request.
	 * That's because nativeArena field is managed by GC.
	 * If the RequestData will be cleaned up by GC, all subsequent answer will fail the JVM.
	 *
	 * @param <R> the type parameter
	 */
	private record RequestData<R>(boolean hasResponse,
	                              Arena nativeArena,
	                              ReentrantLock queueLock,
	                              Class<R> responseClass,
	                              CompletableFuture<R> responseFuture,
	                              Consumer<JsonNode> subscriptionHandler,
	                              AppObject appObject) {
	}

	/**
	 * The type Subscription handler.
	 */
	record SubscriptionHandler(Consumer<JsonNode> eventAction) {
	}
}
