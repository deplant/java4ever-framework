package tech.deplant.java4ever.binding.ffi;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

public class NativeMethods {

	public static String tcCreateContext(String configJson) {
		try (Arena nativeMemory = Arena.ofShared()) {
			MemorySegment handle = ton_client.tc_create_context(NativeStrings.toRust(configJson, nativeMemory));
			String s = NativeStrings.toJava(
					ton_client.tc_read_string(
							nativeMemory::allocate,
							handle
					)
			);
			ton_client.tc_destroy_string(handle);
			return s;
		}
	}

	public static void tcDestroyContext(int contextId) {
			ton_client.tc_destroy_context(contextId);
	}

	public static void tcRequest(int contextId,
	                             final String functionName,
	                             final String params,
	                             final Arena nativeMemory,
	                             int requestId,
	                             final tc_response_handler_t.Function handler) {
			ton_client.tc_request(contextId,
			                      NativeStrings.toRust(functionName, nativeMemory),
			                      NativeStrings.toRust(params, nativeMemory),
			                      requestId,
			                      tc_response_handler_t.allocate(handler, nativeMemory));
		}

}
