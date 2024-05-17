package org.example;

import twomillions.other.cryptoverifier.entrypoint.EntryPointInterface;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.validators.impl.ValidatorManager;

/**
 * 插件入口，应实现 {@link EntryPointInterface} 重写 onLoad 方法。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/15
 */
@SuppressWarnings("unused")
public class Main implements EntryPointInterface {
    /**
     * 入口方法。
     */
    @Override
    public void onLoad() {
        LoggerUtils.getLogger().info("[Plugin] 插件已加载!");
        ValidatorManager.getValidatorManager().registerValidator(new Validation());
    }
}
