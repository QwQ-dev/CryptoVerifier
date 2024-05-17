package twomillions.other.cryptoverifier.commands.impl.interfaces;

/**
 * 定义命令接口。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/27
 */
@SuppressWarnings("unused")
public interface Command {
    /**
     * 处理命令。
     *
     * <p>
     * 格式为: {@code main sub sub}，该方法将获取到 {@code sub sub} 数组。
     * </p>
     */
    void handleCommand(String[] args) throws Exception;

    /**
     * 获取主命令。
     *
     * <p>
     * 格式为: {@code main sub sub}，该方法将获取 {@code main} 字段。
     * </p>
     *
     * @return 主命令
     */
    String getMainCommand();

    /**
     * 对于此命令的描述。
     *
     * @return 描述
     */
    String getDescription();

    /**
     * 获取命令执行线程。
     *
     * @return 获取命令执行线程
     */
    default Thread getThread() {
        return Thread.currentThread();
    }
}
