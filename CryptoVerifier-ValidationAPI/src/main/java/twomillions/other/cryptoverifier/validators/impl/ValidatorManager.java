package twomillions.other.cryptoverifier.validators.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;
import twomillions.other.cryptoverifier.validators.impl.interfaces.ValidatorAPI;
import twomillions.other.cryptoverifier.validators.impl.objects.ValidatorResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 验证器管理器。提供注册验证器与进行验证的方法。
 *
 * <p>
 * 使用单例模式实现，需通过静态方法 {@link ValidatorManager#getValidatorManager()} 获取实例。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/14
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorManager implements ValidatorAPI {
    /**
     * 单例模式下的验证器管理器实例。
     */
    @Getter
    private static final ValidatorManager validatorManager = new ValidatorManager();

    /**
     * 已注册管理器列表。
     */
    @Getter
    private final List<RegisteredValidator> validators = new ArrayList<>();

    /**
     * 注册验证器。
     *
     * @param validator {@link Validator}
     */
    @Override
    public void registerValidator(Validator validator) {
        if (validator.getWeight() < 1) {
            LoggerUtils.getLogger().warn("警告: Validator ({}) 正在注册权重为 {} 的验证器。", validator.getName(), validator.getWeight());
        }

        validators.add(new RegisteredValidator(validator, validator.getWeight(), validator.getName()));
    }

    /**
     * 调用验证器进行验证。
     *
     * @param ip                              客户端 IP 地址
     * @param credentialData                  凭证数据对象
     * @param clientVerifierDataObject        客户端验证数据对象
     * @param serverVerifierDataObject        服务器验证数据对象
     * @return 验证结果
     */
    @Override
    @SneakyThrows
    public ValidatorResult callValidators(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) {
        ValidatorResult validatorResult = new ValidatorResult();
        List<RegisteredValidator> sortedValidators = new ArrayList<>(validators);

        if (sortedValidators.isEmpty()) {
            LoggerUtils.getLogger().error("Validators 为空, 这不应该出现! 将会向客户端 {} 返回 {} 结果!", ip, "ERROR");
            return null;
        }

        sortedValidators.sort(Comparator.comparingInt(RegisteredValidator::getWeight));

        Object newServerVerifierDataObject = serverVerifierDataObject;

        for (RegisteredValidator registeredValidator : sortedValidators) {
            try {
                if (!registeredValidator.getValidator().validate(ip, credentialData, clientVerifierDataObject, serverVerifierDataObject)) {
                    return validatorResult.setResultString(registeredValidator.getValidator().getFailedMessage());
                }

                Object registeredValidatorServerVerifierData = registeredValidator.getValidator().getServerVerifierData();
                if (registeredValidatorServerVerifierData != null) {
                    newServerVerifierDataObject = registeredValidatorServerVerifierData;
                }
            } catch (Exception exception) {
                if (!registeredValidator.getValidator().isIgnoreException()) {
                    LoggerUtils.getLogger().error("处理 {} 时候出现错误!", registeredValidator.getValidator().getName(), exception);
                }

                return validatorResult.setResultString(registeredValidator.getValidator().getFailedMessage());
            }
        }

        validatorResult.setServerVerifierData(newServerVerifierDataObject);

        return validatorResult.setResultString("SUCCESS");
    }
}
