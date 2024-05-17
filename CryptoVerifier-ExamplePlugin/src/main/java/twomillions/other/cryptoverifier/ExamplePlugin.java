package twomillions.other.cryptoverifier;

import twomillions.other.cryptoverifier.entrypoint.EntryPointInterface;
import twomillions.other.cryptoverifier.events.impl.EventManager;
import twomillions.other.cryptoverifier.validators.impl.ValidatorManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;

/**
 * 插件入口，应实现 {@link EntryPointInterface} 重写 onLoad 方法。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
@SuppressWarnings("unused")
public class ExamplePlugin implements EntryPointInterface {
    /**
     * 入口方法。
     */
    @Override
    public void onLoad() {
        /*
         * 注册监听器，传入类
         */
        LoggerUtils.getLogger().info("[ExamplePlugin] 插件已加载!");

        EventManager.getEventManager().registerListener(new ExampleListener());
        LoggerUtils.getLogger().info("[ExamplePlugin] 监听器已成功注册!");

        ValidatorManager.getValidatorManager().registerValidator(new ExampleValidation());
        LoggerUtils.getLogger().info("[ExamplePlugin] 自定义验证器已成功注册!");
    }
}
