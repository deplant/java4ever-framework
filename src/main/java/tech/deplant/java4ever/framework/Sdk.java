package tech.deplant.java4ever.framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import tech.deplant.java4ever.binding.Client;
import tech.deplant.java4ever.binding.Context;
import tech.deplant.java4ever.binding.loader.LibraryLoader;

public record Sdk(Context context, long timeout, Double version, ObjectMapper mapper) {

    public final static String[] DEFAULT_ENDPOINTS = Network.DEV_NET.endpoints();
    public final static ObjectMapper DEFAULT_MAPPER = JsonMapper.builder() // or different mapper for other format
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            // and possibly other configuration, modules, then:
            .build();

    public static Sdk ofContext(Context context, long timeout, ObjectMapper mapper) {
        var verString = Client.version(context).version();
        var ver2 = Double.valueOf(verString.substring(0, verString.lastIndexOf(".")));
        return new Sdk(context, timeout, ver2, mapper);
    }

    public static Sdk ofContextConfig(LibraryLoader loader, Client.ClientConfig config, long timeout, ObjectMapper mapper) {
        try {
            return ofContext(Context.create(loader, DEFAULT_MAPPER.writeValueAsString(config)), timeout, mapper);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T request(T response) throws SdkException {
        //final Sdk.SdkException[] exSdk = new Sdk.SdkException[1];
        try {
            return response;//.get(this.timeout, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            log.error("Library call interrupted! " + e.getMessage());
//            throw new SdkException(new Error(-499, "Library request interrupted!"));
        } catch (Throwable e) {
            throw new SdkException(new Error(-100, "Response parsing exception: " + e.getMessage()));
            //log.error("Library call interrupted! " + e.getMessage());
            //throw new SdkException(new Error(-499, "Library request interrupted!"));
//        } catch (ExecutionException e) {
//            //log.error("Library call execution fail! " + e.getMessage());
//            log.error(e.getCause().getMessage());
//            throw new SdkException(new Gson().fromJson(e.getCause().getMessage(), Error.class));
//        } catch (TimeoutException e) {
//            log.error("Library call timeout! " + e.getMessage());
//            throw new SdkException(new Error(-408, "Library request timeout!"));
        }

    }

    public enum Network {

        MAIN_NET(new String[]{
                "https://eri01.main.everos.dev/",
                "https://gra01.main.everos.dev/",
                "https://gra02.main.everos.dev/",
                "https://lim01.main.everos.dev/",
                "https://rbx01.main.everos.dev/"
        }),

        DEV_NET(new String[]{
                "https://eri01.net.everos.dev/",
                "https://rbx01.net.everos.dev/",
                "https://gra01.net.everos.dev/"
        }),
        LOCALHOST(new String[]{
                "http://0.0.0.0/",
                "http://127.0.0.1/",
                "http://localhost/"
        });

        String[] endpoints;

        Network(String[] endpoints) {
            this.endpoints = endpoints;
        }

        public String[] endpoints() {
            return this.endpoints;
        }
    }

    public record Error(int code, String message) {
    }

    public static class SdkException extends RuntimeException {

        private Error error;

        public SdkException(Error error) {
            this.error = error;
        }

        public Error error() {
            return this.error;
        }

        @Override
        public String toString() {
            return "Code: " + this.error.code() + "\nMessage: " + this.error.message();
        }
    }

}
