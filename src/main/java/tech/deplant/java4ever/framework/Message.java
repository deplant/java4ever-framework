package tech.deplant.java4ever.framework;

import tech.deplant.java4ever.binding.Processing;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Message {

    Function<String, String> textToSolidity = Data::strToHex;
    Function<String, String> textFromSolidity = Data::hexToStr;
    Function<Long, Instant> dateFromSolidity = Instant::ofEpochSecond;
    Function<Instant, Long> dateToSolidity = Instant::getEpochSecond;

    public static Map<String, Object> decodeOutputMessage(Processing.DecodedOutput decoded) {
        if (decoded.output().isPresent()) {
            return decoded.output().get();
        } else {
            return new HashMap<>();
        }
    }

//    public static EncodeMessage.Params callMessageParams(Abi abi, String address, KeyPair keys, String functionName, Map<String, Object> functionInput) {
//        return encodeMessage.Params(
//                abi,
//                address,
//                null,
//                new CallSet(functionName, null, functionInput),
//                Signer.signWithKeyPair(keys),
//                null
//        );
//    }
//
//    public static EncodeMessage.Params deployMessageParams(Abi abi, ContractTvc contractTvc, int workchainId, String initialData, KeyPair keys, Map<String, Object> constructorInput) {
//        return new EncodeMessage.Params(
//                abi,
//                null,
//                new DeploySet(contractTvc.tvcString(), workchainId, initialData, keys.pk()),
//                new CallSet("constructor", null, constructorInput),
//                Signer.signWithKeyPair(keys),
//                null
//        );
//    }
//
//    private Abi.ResultOfEncodeMessage encode(@NonNull Sdk sdk, Abi abi, Address address, @NonNull String abiFunction, Map<String, Object> input) {
//        return sdk.syncCall(Abi.encodeMessage(
//                sdk().context(),
//                this.abi,
//                this.address.makeAddrStd(),
//                null,
//                new Abi.CallSet(
//                        abiFunction,
//                        null,
//                        input
//                ),
//                Abi.Signer.None,
//                null
//        ));
//    }

}
