package twomillions.other.cryptoverifier.command.handlers;

import twomillions.other.cryptoverifier.annotations.CommandAutoRegister;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.io.databases.DataManager;
import twomillions.other.cryptoverifier.security.detectors.ParallelLimitDetector;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.util.general.TimeUtils;

@CommandAutoRegister
public class GetInfo implements Command {
    @Override
    @SuppressWarnings("DataFlowIssue")
    public void handleCommand(String[] args) throws Exception {
        String uuid = args[0];

        if (!DataManager.get().contains(uuid)) {
            LoggerUtils.getLogger().info("UUID: {} 不存在, 请检查大小写。", uuid);
            return;
        }

        ServerVerifierData serverVerifierData = ServerVerifierData.fromString(DataManager.get(uuid));

        boolean isBanned = serverVerifierData.isTempBanned();
        String bannedInfo = serverVerifierData.getTempBannedInfo();

        String lastUsedTimeString = serverVerifierData.getLastUsedTime();
        String lastUsedTime = lastUsedTimeString.isEmpty() ? "从未使用" : TimeUtils.convertLongToDateString(lastUsedTimeString);

        int ipSize = serverVerifierData.getIps().size();
        String ipLimit = serverVerifierData.getIpLimit() == 0 ? "无限制" : String.valueOf(serverVerifierData.getIpLimit());

        int hwidSize = serverVerifierData.getHwids().size();
        String hwidLimit = serverVerifierData.getHwidLimit() == 0 ? "无限制" : String.valueOf(serverVerifierData.getHwidLimit());

        int verificationTimes = serverVerifierData.getVerificationTimes();
        String verificationLimit = serverVerifierData.getVerificationLimit() == 0 ? "无限制" : String.valueOf(serverVerifierData.getVerificationLimit());

        String parallelSize = String.valueOf(ParallelLimitDetector.getParallelLimitDetector().getKeys(uuid).size());
        String parallelLimit = serverVerifierData.getParallelLimit() == 0 ? "无限制" : String.valueOf(serverVerifierData.getParallelLimit());

        String verificationInfo = serverVerifierData.getVerificationInfo();
        String fileName = serverVerifierData.getFileName();
        String creationData = TimeUtils.convertLongToDateString(serverVerifierData.getCreationDate());

        String expiration = serverVerifierData.getExpirationDate();
        String expirationDate = expiration.equals("0") ? "永不过期" : TimeUtils.convertLongToDateString(expiration);

        LoggerUtils.sendEmptyInfo();

        LoggerUtils.getLogger().info("临时封禁: {}", isBanned);

        if (isBanned) {
            LoggerUtils.getLogger().info("封禁信息: {}", bannedInfo);
        }

        LoggerUtils.getLogger().info("最近一次成功验证时间: {}", lastUsedTime);

        LoggerUtils.sendEmptyInfo();

        LoggerUtils.getLogger().info("IP 数: {}/{}, HWID 数: {}/{}, 使用次数: {}/{}, 并行数: {}/{}", ipSize, ipLimit, hwidSize, hwidLimit, verificationTimes, verificationLimit, parallelSize, parallelLimit);

        if (!serverVerifierData.getIps().isEmpty()) {
            LoggerUtils.sendEmptyInfo();
            LoggerUtils.getLogger().info("IP 列表: ");

            for (String ip : serverVerifierData.getIps()) {
                LoggerUtils.getLogger().info("  {}", ip);
            }
        }

        LoggerUtils.sendEmptyInfo();

        LoggerUtils.getLogger().info("验证信息: {}", verificationInfo);
        LoggerUtils.getLogger().info("文件名称: {}", fileName);
        LoggerUtils.getLogger().info("创建时间: {}", creationData);
        LoggerUtils.getLogger().info("过期时间: {}", expirationDate);

        LoggerUtils.sendEmptyInfo();
    }

    @Override
    public String getMainCommand() {
        return "getInfo";
    }

    @Override
    public String getDescription() {
        return "<UUID> - 获取指定许可证目前的使用情况, 在线 IP HWID 数等更多信息。";
    }
}
