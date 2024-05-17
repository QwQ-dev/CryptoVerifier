package twomillions.other.cryptoverifier.commands.impl.handlers;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import twomillions.other.cryptoverifier.commands.impl.CommandManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;

/**
 * 命令处理器，用于启动命令行界面和处理用户输入的命令。
 *
 * <p>
 * 使用单例模式实现，通过 {@link CommandHandler#getCommandHandler()} 方法获取唯一实例。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/27
 */
public class CommandHandler implements twomillions.other.cryptoverifier.commands.impl.interfaces.CommandHandler {
    /**
     * 单例模式下的命令处理器实例。
     */
    @Getter
    private static final CommandHandler commandHandler = new CommandHandler();

    /**
     * 开始处理。
     */
    @Override
    public void start() {
        getCommandHandler().handle();
    }

    /**
     * 处理命令。
     */
    @SneakyThrows
    @SuppressWarnings("InfiniteLoopStatement")
    private void handle() {
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        String[] mainCommands = CommandManager.getCommandManager()
                .getRegisteredCommand()
                .stream()
                .map(registeredCommand -> registeredCommand.getCommand().getMainCommand())
                .toArray(String[]::new);

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .completer(new StringsCompleter(mainCommands))
                .build();

        LoggerUtils.getLogger().info("开始处理命令, 您可以使用 'help' 查看命令帮助");

        while (true) {
            String userInput = lineReader.readLine("> ");

            if (userInput.isEmpty()) {
                continue;
            }

            CommandManager.getCommandManager().callCommand(userInput);
        }
    }
}
