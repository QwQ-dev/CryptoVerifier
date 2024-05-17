package twomillions.other.cryptoverifier.command.handlers;

import org.apache.commons.io.FileUtils;
import twomillions.other.cryptoverifier.annotations.CommandAutoRegister;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;
import twomillions.other.cryptoverifier.crypto.verifier.data.ClientVerifierData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.io.databases.DataManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

@CommandAutoRegister
public class GetFileInfo implements Command {
    @Override
    @SuppressWarnings("DataFlowIssue")
    public void handleCommand(String[] args) throws Exception {
        ClientVerifierData clientVerifierData = ClientVerifierData.fromString(
                FileUtils.readFileToString(new File(args[0])
                        , StandardCharsets.UTF_8)
        );

        String clientUUID = clientVerifierData.getUuid();

        try {
            LoggerUtils.sendEmptyInfo();
            LoggerUtils.getLogger().info("文件 UUID: {}", clientUUID);

            ServerVerifierData serverVerifierData = ServerVerifierData.fromString(DataManager.get(clientUUID));

            LoggerUtils.getLogger().info("文件验证信息: {}", clientVerifierData.getVerificationInfo(serverVerifierData));
            LoggerUtils.getLogger().info("文件创建时间: {}", clientVerifierData.getCreationDate(serverVerifierData));
        } catch (Exception exception) {
            LoggerUtils.getLogger().error("解析文件验证信息等内容时出现错误, 这个文件是正确的吗? 解密配置匹配吗?");
        } finally {
            LoggerUtils.sendEmptyInfo();
        }

        if (DataManager.get(clientUUID) == null) {
            LoggerUtils.getLogger().info("通过 UUID 无法查找到服务端对应数据, 该 UUID 数据不存在。");
        } else {
            LoggerUtils.getLogger().info("可通过 UUID 查找到服务端对应数据, 建议您使用 getInfo 命令详细查看该许可证信息。");
        }

        LoggerUtils.sendEmptyInfo();
    }

    @Override
    public String getMainCommand() {
        return "getFileInfo";
    }

    @Override
    public String getDescription() {
        return "<文件路径> - 获取指定许可证文件的信息。";
    }
}
