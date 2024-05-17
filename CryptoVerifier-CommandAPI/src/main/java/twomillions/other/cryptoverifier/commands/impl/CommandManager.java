package twomillions.other.cryptoverifier.commands.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;
import twomillions.other.cryptoverifier.commands.impl.interfaces.CommandAPI;
import twomillions.other.cryptoverifier.util.LoggerUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 命令管理器。提供注册命令与触发命令的方法。
 *
 * <p>
 * 使用单例模式实现，需通过静态方法 {@link CommandManager#getCommandManager()} 获取实例。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/27
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandManager implements CommandAPI {
    /**
     * 单例模式下的命令管理器实例。
     */
    @Getter
    private static final CommandManager commandManager = new CommandManager();

    /**
     * 已注册命令列表。
     */
    @Getter
    private final List<RegisteredCommand> registeredCommand = new ArrayList<>();

    /**
     * 注册命令。
     *
     * @param command {@link Command}
     */
    @Override
    public void registerCommand(Command command) {
        registeredCommand.add(new RegisteredCommand(command));
    }

    /**
     * 触发命令。
     */
    @Override
    @SneakyThrows
    public void callCommand(String commands) {
        if (commands.isEmpty()) {
            return;
        }

        String[] commandSplit = commands.split(" ");

        String mainCommand = commandSplit[0];
        String[] args = Arrays.stream(commandSplit, 1, commandSplit.length)
                .toArray(String[]::new);

         RegisteredCommand triggeredRegisterCommand = registeredCommand.stream()
                .filter(command -> command.getCommand().getMainCommand().equalsIgnoreCase(mainCommand))
                .findFirst()
                .orElse(null);

        if (triggeredRegisterCommand == null) {
            LoggerUtils.getLogger().info("未知命令: {}, 请检查命令是否正确。", commands);
            return;
        }

        Command triggeredCommand = triggeredRegisterCommand.getCommand();

        try {
            triggeredCommand.handleCommand(args);
        } catch (Exception exception) {
            LoggerUtils.getLogger().info("命令执行错误: {}.", exception.getMessage());
        }
    }
}
