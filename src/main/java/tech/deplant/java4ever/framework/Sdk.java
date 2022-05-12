package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Log4j2
@Value
public class Sdk {

    Context context;
    long timeout;
    Double version;
    ObjectMapper mapper;

    public static Sdk ofContext(Context context, long timeout, ObjectMapper mapper) {
        try {
            var verString = Client.version(context).get(timeout, TimeUnit.SECONDS).version();
            var ver2 = Double.valueOf(verString.substring(0, verString.lastIndexOf(".")));
            return new Sdk(context, timeout, ver2, mapper);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Sdk ofContextConfig(LibraryLoader loader, Context.Config config, long timeout, ObjectMapper mapper) {
        return ofContext(new Context(loader, config.toJson()), timeout, mapper);
    }

    public <T> T syncCall(CompletableFuture<T> future) throws SdkException {
        final Sdk.SdkException[] exSdk = new Sdk.SdkException[1];
        try {
            return future.get(this.timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Library call interrupted! " + e.getMessage());
            throw new SdkException(new Error(-499, "Library request interrupted!"));
        } catch (ExecutionException e) {
            //log.error("Library call execution fail! " + e.getMessage());
            log.error(e.getCause().getMessage());
            throw new SdkException(new Gson().fromJson(e.getCause().getMessage(), Error.class));
        } catch (TimeoutException e) {
            log.error("Library call timeout! " + e.getMessage());
            throw new SdkException(new Error(-408, "Library request timeout!"));
        }
//        try {
//            future.handle((msg, ex) -> {
//                if (ex != null) {
//                    exSdk[0] = new SdkException(new Gson().fromJson(ex.getMessage(), Error.class));
//                    return null;
//                } else {
//                    return msg;
//                }
//            });
//            T result = future.get(this.timeout, TimeUnit.SECONDS);
//            if (exSdk[0] != null) {
//                throw exSdk[0];
//            }
//            return result;
//

    }

    @Value
    public static class Error {
        int code;
        String message;
    }

    @Value
    public static class SdkException extends RuntimeException {
        Error error;

        @Override
        public String toString() {
            return "Code: " + this.error.code() + "\nMessage: " + this.error.message();
        }
    }

}
