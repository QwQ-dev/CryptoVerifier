package twomillions.other.cryptoverifier.command.handlers;

import twomillions.other.cryptoverifier.annotations.CommandAutoRegister;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;
import twomillions.other.cryptoverifier.crypto.verifier.Verifier;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.util.general.TimeUtils;

@CommandAutoRegister
public class Create implements Command {
    @Override
    public void handleCommand(String[] args) throws Exception {
        String uuid = args[0];
        String info = args[1];

        int hwidLimit = Integer.parseInt(args[2]);
        int ipLimit = Integer.parseInt(args[3]);
        int verificationLimit = Integer.parseInt(args[4]);
        int parallelLimit = Integer.parseInt(args[5]);

        String expirationDataString = args[6];
        long expirationData = expirationDataString.equals("0") ? 0 : TimeUtils.convertDateStringToLong(expirationDataString);

        String showExpirationData = expirationData == 0L ? "永不过期" : String.valueOf(expirationData);

        String clientFileName = args[7];

        if (!Verifier.create(uuid, info, hwidLimit, ipLimit, verificationLimit, parallelLimit, expirationData, clientFileName)) {
            LoggerUtils.getLogger().warn("创建失败, 读写权限不足? 控制台是否具有报错?");
            return;
        }

        LoggerUtils.getLogger().info("创建完成! UUID: {}, Info: {}, HWID 限制数: {}, IP 限制数: {}, 使用限制次数: {}, 并行限制数: {}, 许可证过期时间: {}, 客户端授权文件名: {}."
                , uuid, info, hwidLimit, ipLimit, verificationLimit, parallelLimit, showExpirationData, clientFileName);
    }

    @Override
    public String getMainCommand() {
        return "create";
    }

    @Override
    public String getDescription() {
        return "<UUID> <Info> <HWID 限制数> <IP 限制数> <使用限制次数> <并行限制数> <许可证过期时间 (????/??/??)> <客户端授权文件名> - 创建新许可证 (若不需要限制或过期则填写 0 即可)。";
    }
}
