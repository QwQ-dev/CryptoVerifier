package twomillions.other.cryptoverifier.validators.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

/**
 * 已注册验证器对象。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/14
 */
@Getter
@AllArgsConstructor
public class RegisteredValidator {
    /**
     * 实现 {@link Validator} 的类。
     */
    private final Validator validator;

    /**
     * 权重，越小越先执行。
     */
    private final int weight;

    /**
     * 验证器名称。
     */
    private final String name;
}
