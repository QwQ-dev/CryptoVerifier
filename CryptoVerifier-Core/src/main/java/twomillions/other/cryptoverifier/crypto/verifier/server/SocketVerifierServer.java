package twomillions.other.cryptoverifier.crypto.verifier.server;

import lombok.Cleanup;
import twomillions.other.cryptoverifier.communication.ValidationInfoData;
import twomillions.other.cryptoverifier.crypto.sharedkey.DiffieHellmanGenerator;
import twomillions.other.cryptoverifier.communication.handlers.objects.RequestContext;
import twomillions.other.cryptoverifier.communication.handlers.RequestHandler;
import twomillions.other.cryptoverifier.communication.data.RequestData;
import twomillions.other.cryptoverifier.communication.data.SensitiveData;
import twomillions.other.cryptoverifier.communication.enums.RequestHeader;
import twomillions.other.cryptoverifier.crypto.verifier.enums.VerifierServerType;
import twomillions.other.cryptoverifier.events.custom.VerifierServerStartEvent;
import twomillions.other.cryptoverifier.events.impl.EventManager;
import twomillions.other.cryptoverifier.crypto.verifier.interfaces.VerifierServerInterface;
import twomillions.other.cryptoverifier.io.yaml.YamlManager;
import twomillions.other.cryptoverifier.security.detectors.FastVerifierDetector;
import twomillions.other.cryptoverifier.thread.VerifierThreadPool;
import twomillions.other.cryptoverifier.util.LoggerUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;

public class SocketVerifierServer implements VerifierServerInterface {
    @Override
    public boolean start() {
        try {
            int port = YamlManager.getConfig().getInt("PORT");

            VerifierServerStartEvent verifierServerStartEvent =
                    new VerifierServerStartEvent(port, VerifierServerType.Socket);
            EventManager.getEventManager().callEvent(verifierServerStartEvent);

            startHandleClientMessage(new ServerSocket(port));
            FastVerifierDetector.getFastVerifierDetector().initializeAndStart();

            LoggerUtils.getLogger().info("[Socket] 开始处理验证任务, 监听端口为: {}", port);

            return true;
        } catch (Exception exception) {
            LoggerUtils.getLogger().error("[Socket] 开启任务错误。", exception);
            return false;
        }
    }

    private void startHandleClientMessage(ServerSocket serverSocket) {
        VerifierThreadPool.getVerifierThreadPool().runAsync(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    VerifierThreadPool.getVerifierThreadPool().runAsync(() -> handle(socket));
                } catch (IOException exception) {
                    LoggerUtils.getLogger().error("[Socket] 接受客户端连接错误!", exception);
                    break;
                }
            }
        });
    }

    private void handle(Socket socket) {
        ValidationInfoData validationInfoData = new ValidationInfoData()
                .setStartTime(System.currentTimeMillis())
                .setIp(socket.getInetAddress().getHostAddress())
                .setUuid("未获取")
                .setResult("UNKNOWN");

        RequestContext requestContext = new RequestContext()
                .setSocket(socket)
                .setValidationInfoData(validationInfoData);

        String ip = validationInfoData.getIp();
        String uuid = validationInfoData.getUuid();

        try {
            handleMessage(requestContext);
            uuid = validationInfoData.getUuid();
        } catch (SocketTimeoutException exception) {
            LoggerUtils.getLogger().warn("[Socket] [{}] [{}] 客户端超时未发送消息, 正在断开连接", uuid, ip);
        } catch (SocketException exception) {
            LoggerUtils.getLogger().warn("[Socket] [{}] [{}] 验证时客户端中断, 正在断开连接", uuid, ip);
        } catch (Exception ignore) {
            // ignore
        }
    }

    private void handleMessage(RequestContext requestContext) throws Exception {
        @Cleanup Socket socket = requestContext.getSocket();
        @Cleanup BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        @Cleanup PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

        KeyPair serverKeyPair = DiffieHellmanGenerator.generateKeyPair();
        PublicKey serverPublicKey = serverKeyPair.getPublic();

        SensitiveData sensitiveData = new SensitiveData()
                .setServerPublicKey(serverPublicKey);

        requestContext
                .setBufferedReader(bufferedReader)
                .setPrintWriter(printWriter)
                .setServerKeyPair(serverKeyPair)
                .setSensitiveData(sensitiveData);

        socket.setSoTimeout(YamlManager.getConfig().getInt("SOCKET-TIME-OUT"));

        String clientRequestDataString;

        while ((clientRequestDataString = bufferedReader.readLine()) != null) {
            RequestData clientRequestData = RequestData.fromString(clientRequestDataString);
            RequestHeader clientRequestDataHeader = clientRequestData.getRequestHeader();
            String clientRequestDataMessage = clientRequestData.getMessage();

            requestContext
                    .setClientRequestDataHeader(clientRequestDataHeader)
                    .setClientRequestDataMessage(clientRequestDataMessage);

            if (clientRequestDataHeader == RequestHeader.ClientInit) {
                RequestHandler.processClientInitRequest(requestContext);
                continue;
            }

            if (clientRequestDataHeader == RequestHeader.ClientMessage) {
                RequestHandler.processClientMessageRequest(requestContext);
                break;
            }
        }
    }
}
