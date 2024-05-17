package twomillions.other.cryptoverifier.command.handlers;

import twomillions.other.cryptoverifier.annotations.CommandAutoRegister;
import twomillions.other.cryptoverifier.commands.impl.CommandManager;
import twomillions.other.cryptoverifier.commands.impl.RegisteredCommand;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;
import twomillions.other.cryptoverifier.util.LoggerUtils;

import java.util.List;

@CommandAutoRegister
public class Help implements Command {
    @Override
    public void handleCommand(String[] args) {
        List<RegisteredCommand> registeredCommands = CommandManager.getCommandManager().getRegisteredCommand();

        if (registeredCommands.isEmpty()) {
            LoggerUtils.getLogger().error("命令为空, 这是不应该出现的!");
            return;
        }

        for (RegisteredCommand registeredCommand : registeredCommands) {
            Command command = registeredCommand.getCommand();

            LoggerUtils.sendEmptyInfo();
            LoggerUtils.getLogger().info(command.getMainCommand());
            LoggerUtils.getLogger().info(" 命令描述: {}", command.getDescription());
        }
    }

    @Override
    public String getMainCommand() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "帮助。";
    }
}
