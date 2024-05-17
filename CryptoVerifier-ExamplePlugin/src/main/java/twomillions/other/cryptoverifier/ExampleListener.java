package twomillions.other.cryptoverifier;

import org.slf4j.Logger;
import twomillions.other.cryptoverifier.commands.impl.CommandManager;
import twomillions.other.cryptoverifier.events.custom.InitializationCompleteEvent;
import twomillions.other.cryptoverifier.events.impl.annotations.EventHandler;
import twomillions.other.cryptoverifier.events.impl.interfaces.Listener;
import twomillions.other.cryptoverifier.util.LoggerUtils;

/**
 * 一个简单的示例事件监听类，事件监听类应实现 {@link Listener} 接口。
 *
 * <p>
 * 事件请查看 {@link twomillions.other.cryptoverifier.events.custom} 软件包下所有类。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
@SuppressWarnings("unused")
public class ExampleListener implements Listener {
    /**
     * 事件监听器，处理事件。
     * 必须使用 {@link EventHandler} 注解，否则无效。
     *
     * @param event 初始化完成事件
     */
    @EventHandler
    public void testEventHandler(InitializationCompleteEvent event) {
        Logger logger = LoggerUtils.getLogger();
        
        logger.info("--------------------");
        logger.info("出现这条消息代表已经成功触发 InitializationCompleteEvent, 这与 Bukkit Event 类似。");
        logger.info("我们不建议使用 System.out.println 打印日志信息, 在高并发情况下效率极低。");
        logger.info("我们推荐使用日志依赖库, 您可以使用 LoggerUtils.getLogger() 获取 Logger 打印日志。");
        logger.info("--------------------");
        logger.info("大多数事件都是异步的, 此处为初始化完成事件, 为同步事件, 可使用 isSync() 检查是否为同步事件。");
        logger.info("可使用线程池 VerifierThreadPool.getVerifierThreadPool() 进行同步、异步、延迟、重复操作, 与 Bukkit 线程池类似。");
        logger.info("--------------------");
        logger.info("是否同步: " + event.isSync());
        logger.info("线程名称: " + event.getThread().getName());
        logger.info("--------------------");

        /*
         * 使用 CommandManager.getCommandManager().callCommand(String) 方法处理命令
         */
        CommandManager.getCommandManager().callCommand("help");
    }
}
