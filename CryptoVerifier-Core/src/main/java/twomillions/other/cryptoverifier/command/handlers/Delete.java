package twomillions.other.cryptoverifier.command.handlers;

import twomillions.other.cryptoverifier.annotations.CommandAutoRegister;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;
import twomillions.other.cryptoverifier.io.databases.DataManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;

@CommandAutoRegister
public class Delete implements Command {
    @Override
    public void handleCommand(String[] args) throws Exception {
        String uuid = args[0];

        DataManager.delete(uuid);
        LoggerUtils.getLogger().info("已尝试删除 {} 的所有数据", uuid);
    }

    @Override
    public String getMainCommand() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "<UUID> - 删除指定许可证";
    }
}
