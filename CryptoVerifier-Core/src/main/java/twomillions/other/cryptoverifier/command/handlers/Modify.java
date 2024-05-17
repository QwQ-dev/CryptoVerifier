package twomillions.other.cryptoverifier.command.handlers;

import com.google.common.collect.Sets;
import twomillions.other.cryptoverifier.annotations.CommandAutoRegister;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.io.databases.DataManager;
import twomillions.other.cryptoverifier.io.databases.nonpersistent.NonPersistentDatabasesManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.util.common.ConstantsUtils;
import twomillions.other.cryptoverifier.util.general.TimeUtils;

@CommandAutoRegister
public class Modify implements Command {
    @Override
    @SuppressWarnings("DataFlowIssue")
    public void handleCommand(String[] args) throws Exception {
        String action = args[0];
        String property = args[1];
        String uuid = args[2];

        if (!DataManager.get().contains(uuid)) {
            LoggerUtils.getLogger().info("UUID: {} 不存在, 请检查大小写。", uuid);
            return;
        }

        ServerVerifierData data = ServerVerifierData.fromString(DataManager.get(uuid));

        switch (action.toLowerCase()) {
            case "reset":
                switch (property.toLowerCase()) {
                    case "ip":
                        data.setIps(Sets.newConcurrentHashSet());
                        break;
                    case "hwid":
                        data.setHwids(Sets.newConcurrentHashSet());
                        break;
                    case "verificationtimes":
                        data.setVerificationTimes(0);
                        break;
                    case "parallellimit":
                        NonPersistentDatabasesManager.getNonPersistentDatabasesManager().delete(uuid, ConstantsUtils.SERVER_DATA);
                        break;
                    case "ban":
                        data.setTempBanned(true);
                        data.setTempBannedInfo(args.length > 3 ? args[3] : "");
                        LoggerUtils.getLogger().info("已暂时封禁 {}, 信息: {}", uuid, data.getTempBannedInfo());
                        break;
                    case "unban":
                        data.setTempBanned(false);
                        data.setTempBannedInfo("");
                        LoggerUtils.getLogger().info("已解除 {} 的临时封禁", uuid);
                        break;
                    default:
                        LoggerUtils.getLogger().info("Invalid property: {}", property);
                        return;
                }

                LoggerUtils.getLogger().info("已重置 {} 的 {} 属性", uuid, property);
                break;

            case "set":
                if (args.length < 4) {
                    LoggerUtils.getLogger().info("您没有规定新属性: {}", property);
                    return;
                }

                int newLimit;
                String newValue = args[3];

                switch (property.toLowerCase()) {
                    case "ip":
                        newLimit = Integer.parseInt(newValue);
                        data.setIpLimit(newLimit);
                        break;
                    case "hwid":
                        newLimit = Integer.parseInt(newValue);
                        data.setHwidLimit(newLimit);
                        break;
                    case "verificationlimit":
                        newLimit = Integer.parseInt(newValue);
                        data.setVerificationLimit(newLimit);
                        break;
                    case "parallellimit":
                        newLimit = Integer.parseInt(newValue);
                        data.setParallelLimit(newLimit);
                        break;
                    case "expirationdate":
                        data.setExpirationDate(String.valueOf(TimeUtils.convertDateStringToLong(newValue)));
                        break;
                    default:
                        LoggerUtils.getLogger().info("无效属性: {}", property);
                        return;
                }

                LoggerUtils.getLogger().info("已修改 {} 的 {} 为 {}", uuid, property, newValue);
                break;

            default:
                LoggerUtils.getLogger().info("无效动作: {}", action);
                break;
        }

        DataManager.save(uuid, data.toString());
    }

    @Override
    public String getMainCommand() {
        return "modify";
    }

    @Override
    public String getDescription() {
        return "<动作: set/reset> <属性: ip/hwid/verificationTimes/parallelLimit/expirationDate(Only reset)/ban(Only set)/unban(Only set)> <UUID> [新属性 Only set] - 重置或设置指定许可证的属性。";
    }
}
