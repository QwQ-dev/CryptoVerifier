package twomillions.other.cryptoverifier.command.handlers;

import twomillions.other.cryptoverifier.annotations.CommandAutoRegister;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.validators.impl.RegisteredValidator;
import twomillions.other.cryptoverifier.validators.impl.ValidatorManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@CommandAutoRegister
public class GetValidations implements Command {
    @Override
    public void handleCommand(String[] args) {
        List<RegisteredValidator> registeredValidators = new ArrayList<>(ValidatorManager.getValidatorManager().getValidators());

        registeredValidators.sort(Comparator.comparingInt(RegisteredValidator::getWeight));

        if (registeredValidators.isEmpty()) {
            LoggerUtils.getLogger().error("验证器为空, 这是不应该出现的!");
            return;
        }

        for (RegisteredValidator validator : registeredValidators) {
            int weight = validator.getWeight();

            LoggerUtils.sendEmptyInfo();
            LoggerUtils.getLogger().info(validator.getName());
            LoggerUtils.getLogger().info(" 权重: {}", weight == Integer.MAX_VALUE ? "MAX" : weight);
            LoggerUtils.getLogger().info(" 忽略异常: {}", validator.getValidator().isIgnoreException());

            String failedMessage;
            try {
                failedMessage = validator.getValidator().getFailedMessage();
            } catch (Exception exception) {
                failedMessage = "动态";
            }
            LoggerUtils.getLogger().info(" 失败信息: {}", failedMessage);

            String description = validator.getValidator().getDescription();
            LoggerUtils.getLogger().info(" 验证器描述: {}", description.isEmpty() ? "无" : description);
        }

        LoggerUtils.sendEmptyInfo();
    }

    @Override
    public String getMainCommand() {
        return "getValidations";
    }

    @Override
    public String getDescription() {
        return "获取所有已注册的验证器相关信息。";
    }
}
