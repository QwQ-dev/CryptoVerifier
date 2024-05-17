package twomillions.other.cryptoverifier.communication.handlers;

import twomillions.other.cryptoverifier.communication.ValidationInfoData;
import twomillions.other.cryptoverifier.communication.handlers.objects.RequestContext;
import twomillions.other.cryptoverifier.crypto.encryption.aes.AESCryptography;
import twomillions.other.cryptoverifier.crypto.sharedkey.DiffieHellmanGenerator;
import twomillions.other.cryptoverifier.crypto.verifier.Verifier;
import twomillions.other.cryptoverifier.communication.data.RequestData;
import twomillions.other.cryptoverifier.communication.data.SensitiveData;
import twomillions.other.cryptoverifier.communication.enums.RequestHeader;
import twomillions.other.cryptoverifier.crypto.verifier.enums.VerifierServerType;
import twomillions.other.cryptoverifier.events.custom.SocketVerificationCompleteEvent;
import twomillions.other.cryptoverifier.events.impl.EventManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RequestHandler {
    public static void processClientInitRequest(RequestContext requestContext) throws Exception {
        String clientRequestDataMessage = requestContext.getClientRequestDataMessage();

        PrintWriter printWriter = requestContext.getPrintWriter();
        SensitiveData sensitiveData = requestContext.getSensitiveData();

        KeyPair serverKeyPair = requestContext.getServerKeyPair();
        PublicKey serverPublicKey = serverKeyPair.getPublic();
        PrivateKey serverPrivateKey = serverKeyPair.getPrivate();

        PublicKey clientPublicKey = DiffieHellmanGenerator.decodePublicKey(clientRequestDataMessage);
        String serverPublicKeyString = DiffieHellmanGenerator.encode(serverPublicKey);
        String sharedKey = DiffieHellmanGenerator.generateSharedKey(clientPublicKey.getEncoded(), serverPrivateKey.getEncoded());
        SecretKey secretSharedKey = AESCryptography.generateSecretKey(sharedKey);

        sensitiveData
                .setSharedKey(sharedKey)
                .setClientPublicKey(clientPublicKey)
                .setSecretSharedKey(secretSharedKey);

        RequestData serverInitData = new RequestData()
                .setRequestHeader(RequestHeader.ServerInit)
                .setMessage(serverPublicKeyString);

        printWriter.println(serverInitData.toString());
    }

    public static void processClientMessageRequest(RequestContext requestContext) throws Exception {
        String clientRequestDataMessage = requestContext.getClientRequestDataMessage();
        ValidationInfoData validationInfoData = requestContext.getValidationInfoData();

        String ip = validationInfoData.getIp();

        Socket socket = requestContext.getSocket();
        BufferedReader bufferedReader = requestContext.getBufferedReader();
        PrintWriter printWriter = requestContext.getPrintWriter();
        SensitiveData sensitiveData = requestContext.getSensitiveData();


        SecretKey secretSharedKey = sensitiveData.getSecretSharedKey();
        String decryptedClientRequestDataMessage = AESCryptography.decrypt(clientRequestDataMessage, secretSharedKey);

        validationInfoData.setMessage(decryptedClientRequestDataMessage);

        String result = Verifier.verification(validationInfoData)
                .setEndTime(System.currentTimeMillis())
                .getResult();

        String uuid = validationInfoData.getUuid();
        String shortenedResult = result.length() > 25 ? result.substring(0, 25) + " ... (已省略)" : result;

        SocketVerificationCompleteEvent socketVerificationCompleteEvent = new SocketVerificationCompleteEvent(
                socket, bufferedReader, printWriter
        );

        EventManager.getEventManager().callEvent(socketVerificationCompleteEvent);

        if (result.equalsIgnoreCase("BLOCKED_IP")) {
            LoggerUtils.getLogger().warn("[{}] [{}] [{}] 验证次数过多, 对该 IP 进行限制, 返回结果: {}",
                    VerifierServerType.Socket, uuid, ip, shortenedResult
            );
        }

        LoggerUtils.getLogger().info("[{}] [{}] [{}] 验证完成, 即将断开连接, 处理结果: {}, 耗时: {}ms",
                VerifierServerType.Socket, uuid, ip, shortenedResult, validationInfoData.getElapsedTime()
        );

        String encryptedResult = AESCryptography.encrypt(
                result, secretSharedKey
        );

        RequestData serverResultData = new RequestData()
                .setRequestHeader(RequestHeader.ServerResult)
                .setMessage(encryptedResult);

        printWriter.println(serverResultData);
    }
}
