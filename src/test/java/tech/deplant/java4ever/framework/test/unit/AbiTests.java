package tech.deplant.java4ever.framework.test.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.artifact.JsonResource;
import tech.deplant.java4ever.framework.template.MsigTemplate;
import tech.deplant.java4ever.framework.template.Tip31RootTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbiTests {

    @Test
    public void testAbiConvert() throws JsonProcessingException {
        var jsonStr = new JsonResource("artifacts/tip31/TokenRoot.abi.json").get();
        var cachedStr = Tip31RootTemplate.DEFAULT_ABI().json();
        assertEquals(Sdk.DEFAULT_MAPPER.readTree(jsonStr), Sdk.DEFAULT_MAPPER.readTree(cachedStr));
    }

    @Test
    public void testAbiMsigSafe() throws JsonProcessingException {
        assertTrue(MsigTemplate.SAFE_MULTISIG_ABI().hasFunction("acceptTransfer"));
    }

    @Test
    public void testAbiMsigSetcode() throws JsonProcessingException {
        assertTrue(MsigTemplate.SETCODE_MULTISIG_ABI().hasFunction("acceptTransfer"));
    }

    @Test
    public void testAbiMsigSurf() throws JsonProcessingException {
        assertTrue(MsigTemplate.SURF_MULTISIG_ABI().hasFunction("acceptTransfer"));
    }

}
