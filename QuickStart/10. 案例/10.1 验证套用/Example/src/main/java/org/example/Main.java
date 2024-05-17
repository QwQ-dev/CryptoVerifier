package org.example;

import org.apache.commons.io.FileUtils;
import twomillions.other.cryptoverifier.crypto.hardware.HardwareInfoManager;
import twomillions.other.cryptoverifier.data.communication.CredentialData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户端示例类。
 *
 * <p>
 * 所有您需要进行修改或处理的地方均已写上 TODO.
 * </p>
 *
 * <p>
 * 强烈建议对该类进行混淆，以降低代码被逆向工程的风险。如果不进行混淆保护，很容易修改字节码并破解验证。
 * </p>
 *
 * <p>
 * 我们强烈建议使用强大的混淆器对该类进行混淆。推荐将该类的方法混淆为原生 (Native) 代码，并采用其他混淆技术进行进一步保护。
 * 如果将代码混淆为原生 (Native) 代码，建议使用工具对 .dll / .so 等文件进行加壳保护。
 * </p>
 *
 * @author 2000000
 * @version 1.2
 * @see <a href="http://www.vmpsoft.com">vmprotect (付费 加壳工具)</a>
 * @see <a href="https://branchlock.net">branchlock (付费 Java 混淆器)</a>
 * @see <a href="https://www.zelix.com/klassmaster">zelix klassmaster (付费 Java 混淆器)</a>
 * @see <a href="https://allatori.com">allatori (演示版免费但具有水印 Java 混淆器)</a>
 * @see <a href="https://github.com/radioegor146/native-obfuscator">native-obfuscator (免费开源 Java Native 混淆器)</a>
 * @see <a href="https://github.com/skidfuscatordev/skidfuscator-java-obfuscator">skidfuscator-java-obfuscator (社区版免费开源 Java 混淆器)</a>
 * @since 2023/6/17
 */
public class Main {
    /**
     * 验证文件的路径。
     */
    private static final String filePath = "D:/JavaProjects/CryptoVerifier/clientFiles";

    /**
     * 验证文件的文件名。
     */
    private static final String fileName = "test";

    /**
     * 验证服务器 IP 列表。
     * 建议从配置文件中读取这些 IP，以增强灵活性。
     * 多个验证服务器之间可以使用数据库进行通信，形成集群，以防止由于单个服务器无法连接而导致整个验证业务停止的情况。
     */
    private static final List<String> ips = new ArrayList<>();

    /**
     * 主程序入口方法。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        ips.add("127.0.0.1:17354");

        startVerifyThread();
    }

    /**
     * 每隔 5min 发送一次验证请求进行占位。
     *
     * <p>
     * 此处新开线程实现，您也可以使用您自己的线程池，或您为 Minecraft 开发插件，那么也可以使用它们的线程池进行调度。
     * </p>
     */
    @SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
    private static void startVerifyThread() {
        Runnable runnable = () -> {
            Verifier verifier = new Verifier();

            try {
                while (true) {
                    // 我们并没有额外数据发送的要求，留空
                    verify(verifier, "");
                    Thread.sleep(300000);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                System.out.println("验证时出错! 即将退出程序, 请反馈给开发者!");
                System.exit(0);
            }
        };

        new Thread(runnable).start();
    }

    /**
     * 快速验证方法。
     *
     * @param verifier {@link Verifier} 对象
     * @param extraData 额外数据
     * @throws Exception 获取验证内容或验证时发生异常
     */
    private static void verify(Verifier verifier, @SuppressWarnings("SameParameterValue") String extraData) throws Exception {
        File file = new File(filePath, fileName);
        String fileString = FileUtils.readFileToString(file, "UTF-8");
        String hwid = HardwareInfoManager.getMachineIdentifier();

        CredentialData credentialData = new CredentialData(
                fileString, fileName, hwid, extraData
        );

        verifier.setIps(ips);
        verifier.setCredentialData(credentialData);

        verifier.verify();
    }
}