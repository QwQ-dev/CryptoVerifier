package twomillions.other.cryptoverifier.command.handlers;

import twomillions.other.cryptoverifier.annotations.CommandAutoRegister;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;
import twomillions.other.cryptoverifier.io.databases.DataManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;

import java.util.List;

@CommandAutoRegister
public class GetUuids implements Command {
    @Override
    public void handleCommand(String[] args) {
        List<String> uuids = DataManager.get();

        if (uuids == null || uuids.isEmpty()) {
            LoggerUtils.getLogger().info("目前没有已创建的许可证 UUID!");
            return;
        }

        uuids.forEach(uuid -> LoggerUtils.getLogger().info(uuid));
    }

    @Override
    public String getMainCommand() {
        return "getUuids";
    }

    @Override
    public String getDescription() {
        return "获取所有的 UUID。";
    }
}
