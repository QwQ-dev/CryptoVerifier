package org.example;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.latency.LatencyMeasurement;
import org.example.utils.TimeUtils;
import twomillions.other.cryptoverifier.crypto.encryption.aes.AESCryptography;
import twomillions.other.cryptoverifier.crypto.sharedkey.DiffieHellmanGenerator;
import twomillions.other.cryptoverifier.data.communication.CredentialData;
import twomillions.other.cryptoverifier.data.communication.RequestData;
import twomillions.other.cryptoverifier.data.communication.SensitiveData;
import twomillions.other.cryptoverifier.enums.communication.RequestHeader;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

/**
 * 验证对象类。
 *
 * <p>
 * 所有您需要进行修改或处理的地方均已写上 TODO.
 * </p>
 *
 * <p>
 * 该类用于与验证服务器取得联系，进行验证。
 * </p>
 *
 * <p>
 * 强烈建议对该类进行混淆，以降低代码被逆向工程的风险。如果不进行混淆保护，很容易修改字节码并破解验证。
 * </p>
 *
 * <p>
 * 通过使用 {@code @Getter} {@code @Setter} 和 {@code @Accessors(chain = true)} 注解，提供用于获取和设置属性的方法，支持链式调用。
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
public class Verifier {
    /**
     * 验证服务器 ip.
     *
     * <p>
     * 格式为 ip:port 即可。
     * </p>
     */
    private List<String> ips;
    /**
     * 验证凭证。
     */
    private CredentialData credentialData;

    /**
     * 是否已经运行过业务逻辑。
     */
    private boolean loaded = false;

    /**
     * 失败次数。
     */
    private int failed = 0;

    /**
     * 进行一次验证。
     */
    public void verify() {
        String lowestIp = LatencyMeasurement.findLowestLatencyServer(ips);

        if (lowestIp == null) {
            System.out.println("所有的验证服务器均无法连接, 停止验证!");
            System.exit(0);
            return;
        }

        /*
         * 当遇到验证失败则失败次数增加一次 (++ 在前为先增加再使用, 在后为使用后增加)
         * 成功验证直接 return，出现异常则捕获提示后继续运行下面代码，判断失败次数超过三次直接退出
         */
        try {
            String[] splitIp = lowestIp.split(":");
            communicate(splitIp[0], Integer.parseInt(splitIp[1]), credentialData);
            return;
        } catch (UnknownHostException exception) {
            System.out.println("无网络! 累计验证: " + (++ failed) + "/3");
        } catch (SocketException exception) {
            System.out.println("无法连接至验证服务器! 请检查网络连接状态! 累计验证: " + (++ failed) + "/3");
        } catch (Exception exception) {
            System.out.println("验证时出错, 许可证是否正确? 累计验证: " + (++ failed) + "/3");
        }

        if (failed >= 3) {
            System.out.println("累计验证失败三次, 退出程序!");
            System.exit(0);
        }
    }

    /**
     * 对返回内容进行处理。
     *
     * <p>
     * 您需要在这里处理验证逻辑，判断服务器返回的数据等等。
     * </p>
     *
     * @param result 验证服务器返回的内容 (已解密)
     */
    private void handleResult(String result) {
        if (result.equalsIgnoreCase("error")) {
            System.out.println("验证错误, 服务器发送数据不正确? 请反馈给开发者!");
            System.exit(0);
            return;
        }

        String resultToLowerCase = result.toLowerCase();

        if (!resultToLowerCase.contains("success")) {
            System.out.println("许可证验证失败! 请反馈给开发者!");
            System.exit(0);
            return;
        }

        /*
         * 这里的 loaded 变量旨在多次验证时确保只在第一次处理业务逻辑 (加载插件、加载类等等)
         */
        if (!loaded) {
            loaded = true;

            String[] resultSplit = result.split("\\|");

            String uuid = resultSplit[1];
            String expirationDate = resultSplit[2].equals("0") ? "永不过期" : TimeUtils.convertLongToDateString(Long.parseLong(resultSplit[2]));
            String verificationInfo = resultSplit[3];

            System.out.println("验证通过! 用户: " + uuid + ", 过期时间: " + expirationDate + ", 验证信息: " + verificationInfo);

            // 这里是我们的业务逻辑，假设我们的业务逻辑只是输出 Hello world!
            System.out.println("Hello world!");
        }
    }

    /**
     * 服务器无响应时的处理。
     *
     * <p>
     * 若验证服务器不存在问题，那么大概率是黑客在尝试通过本地搭建虚假服务器来破解验证，您需要处理这种情况。
     * </p>
     *
     * <p>
     * 遇到这种情况通常意味着无法与正确的验证服务器取得连接，我们建议删除您的 Jar 与许可证文件来给黑客造成一些麻烦，或直接退出程序。
     * </p>
     */
    private void handleServerUnresponsive() {
        // 直接退出程序
        System.exit(0);
    }

    /**
     * 服务器发送的内容无法被转换成 {@link RequestData} 对象时的处理。
     *
     * <p>
     * 若验证服务器不存在问题，那么大概率是黑客在尝试通过本地搭建虚假服务器来破解验证，您需要处理这种情况。
     * </p>
     *
     * <p>
     * 遇到这种情况通常意味着无法与正确的验证服务器取得连接，我们建议删除您的 Jar 与许可证文件来给黑客造成一些麻烦，或直接退出程序。
     * </p>
     */
    private void handleRequestDataError() {
        // 直接退出程序
        System.exit(0);
    }

    /**
     * 通讯处理。
     *
     * <p>
     * 这段代码并不需要完全理解或进行修改，其主要用于实现验证的整个网络流程。
     * </p>
     *
     * @param ip 验证服务器 ip
     * @param port 验证服务器端口
     * @param credentialData 许可证信息
     * @throws Exception 验证时出现异常
     */
    private void communicate(String ip, int port, CredentialData credentialData) throws Exception {
        // 初始化一个敏感数据对象，用于存储密钥交换内容
        SensitiveData sensitiveData = new SensitiveData();

        // 生成一个密钥对，获取公钥与私钥
        KeyPair clientKeyPair = DiffieHellmanGenerator.generateKeyPair();
        PublicKey clientPublicKey = clientKeyPair.getPublic();
        PrivateKey clientPrivateKey = clientKeyPair.getPrivate();
        String clientPublicKeyString = DiffieHellmanGenerator.encode(clientPublicKey);

        // 初始化一个 Socket 对象，进行连接
        @Cleanup Socket socket = new Socket();
        // 5000ms 为最大等待时间，如果在这个时间内无法建立连接，将会抛出 SocketTimeoutException 异常
        socket.connect(new InetSocketAddress(ip, port), 5000);
        // 10000ms 为读取超时时间，在读取数据时的最大等待时间。如果在这个时间内没有数据可读取，将会抛出 SocketTimeoutException 异常
        socket.setSoTimeout(10000);

        // 初始化输入流，用于从服务器读取数据
        @Cleanup BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // 初始化输出流，用于向服务器发送数据
        @Cleanup PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

        // 创建请求数据对象，设置请求头为 ClientInit，内容为客户端公钥
        RequestData requestData = new RequestData()
                .setRequestHeader(RequestHeader.ClientInit)
                .setMessage(clientPublicKeyString);

        // 发送给服务器
        printWriter.println(requestData);

        String serverRequestDataString;

        // 读取服务器数据，并根据不同的请求头进行处理
        while ((serverRequestDataString = bufferedReader.readLine()) != null) {
            RequestData serverRequestData;

            try {
                serverRequestData = RequestData.fromString(serverRequestDataString);
            } catch (Exception exception) {
                handleRequestDataError();
                return;
            }

            RequestHeader serverRequestDataHeader = serverRequestData.getRequestHeader();
            String serverRequestDataMessage = serverRequestData.getMessage();

            // 如果是 ServerInit 则意味着该次数据内容为服务器公钥
            if (serverRequestDataHeader == RequestHeader.ServerInit) {
                // 解析服务器公钥并生成共享密钥
                PublicKey serverPublicKey = DiffieHellmanGenerator.decodePublicKey(serverRequestDataMessage);
                String sharedKey = DiffieHellmanGenerator.generateSharedKey(serverPublicKey.getEncoded(), clientPrivateKey.getEncoded());
                SecretKey sharedSecretKey = AESCryptography.generateSecretKey(sharedKey);

                sensitiveData
                        .setSharedKey(sharedKey)
                        .setServerPublicKey(serverPublicKey)
                        .setSecretSharedKey(sharedSecretKey);

                // 使用共享密钥加密凭据数据
                String encryptedCredentialData = AESCryptography.encrypt(
                        credentialData.toString(), sharedSecretKey
                );

                // 创建客户端消息数据对象，设置请求头为 ClientMessage，内容为加密后的凭据数据
                RequestData clientMessageData = new RequestData()
                        .setRequestHeader(RequestHeader.ClientMessage)
                        .setMessage(encryptedCredentialData);

                // 发送给服务器
                printWriter.println(clientMessageData);
                continue;
            }

            // 如果是 ServerResult 则意味着该次数据内容为服务器返回的最终内容
            if (serverRequestDataHeader == RequestHeader.ServerResult) {
                String decryptedServerRequestDataMessage;
                SecretKey secretSharedKey = sensitiveData.getSecretSharedKey();

                try {
                    // 使用共享密钥解密服务器结果数据
                    decryptedServerRequestDataMessage = AESCryptography.decrypt(serverRequestDataMessage, secretSharedKey);
                } catch (Exception exception) {
                    decryptedServerRequestDataMessage = "ERROR";
                }

                // 处理解密后的结果数据
                handleResult(decryptedServerRequestDataMessage);
                return;
            }
        }

        // 处理服务器无响应情况
        handleServerUnresponsive();
    }
}
