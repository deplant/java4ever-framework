package tech.deplant.java4ever.framework;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Log4j2
public class FileData {

    public static final String CONTRACTS_PATH = "/artifacts";

    public static String storedContractPath(String fileName) {
        return FileData.CONTRACTS_PATH + "/" + fileName;
    }

    public static byte[] bytesFromFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    public static String stringFromFile(String path) throws IOException {
        log.trace("Accessing file:" + path);
        try {
            return Files.readString(
                    Paths.get(
                            FileData.class.getClassLoader().getResource(path).toURI()
                    ),
                    StandardCharsets.UTF_8
            );
        } catch (URISyntaxException e) {
            log.error("Wrong path: " + path + "Error: " + e.getMessage());
            return null;
        }
    }

    public static String jsonFromFile(String path) throws IOException {
        return stringFromFile(path).replaceAll("[\u0000-\u001f]", "");
    }

    public static String fileToBase64String(String path) {
        try {
            return Base64.getEncoder().encodeToString(bytesFromFile(path));
        } catch (IOException e) {
            log.error("Path: {}, Error: {}", () -> path, () -> e.getMessage());
            return "";
        }
    }


//    public static String getAbiFromFile(String filename) {
//        try {
//            return IOUtils.toString(new ClassPathResource(filename).getInputStream(), StandardCharsets.UTF_8.name()).replaceAll("[\u0000-\u001f]", "");
//        } catch (IOException e) {
//            log.error("ABI File getting exception! {}" ,() -> e.getMessage());
//            return "{}";
//        }
//    }

}
