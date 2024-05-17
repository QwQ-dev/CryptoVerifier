package twomillions.other.cryptoverifier.commands.impl.interfaces;

/**
 * 接口类。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/27
 */
public interface CommandAPI {
    /**
     * 注册命令。
     *
     * @param command {@link Command}
     */
    void registerCommand(Command command);

    /**
     * 触发命令。
     *
     * @param commands 全部命令的字符串形式
     */
    void callCommand(String commands);
}
