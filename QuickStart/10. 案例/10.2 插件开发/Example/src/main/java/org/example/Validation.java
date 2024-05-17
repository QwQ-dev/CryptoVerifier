package org.example;

import lombok.NonNull;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierDataAPI;
import twomillions.other.cryptoverifier.data.communication.CredentialData;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

/**
 * 示例自定义验证器，应实现 {@link Validator} 重写所有方法。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/15
 */
public class Validation implements Validator {
    /**
     * 验证方法。
     *
     * <p>
     * 注意，此处的 {@code serverVerifierDataObject} 会持续更新，
     * 保证下一个验证器收到的是上一个验证器返回的最新数据。但真正的 IO 操作只有所有验证通过才会进行。
     * </p>
     *
     * @param ip                              客户端 IP 地址
     * @param credentialData                  凭证数据对象
     * @param clientVerifierDataObject        客户端验证数据对象
     * @param serverVerifierDataObject        服务器验证数据对象
     * @return 验证结果
     */
    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) throws Exception {
        ServerVerifierDataAPI serverVerifierDataAPI = new ServerVerifierDataAPI(serverVerifierDataObject);

        String extraData = credentialData.getExtraData();
        String verificationInfo = serverVerifierDataAPI.getVerificationInfo();

        if (extraData == null || extraData.isEmpty()) {
            return false;
        }

        return verificationInfo.contains(extraData);
    }

    /**
     * 权重，越低越先执行。
     *
     * <p>
     * 范围在 {@code 20} ~ {@code 10000} 之间。
     * </p>
     *
     * <p>
     * 虽然您可以注册 {@code 20} 以下的权重，但是为了安全期间，我永远不会推荐您这么做，除非您真的知道自己在做什么。
     * </p>
     *
     * @return 权重
     */
    @Override
    public int getWeight() {
        return 20;
    }

    /**
     * 是否忽略异常。
     *
     * <p>
     * 若为真，当 {@link Validator#validate(String, CredentialData, Object, Object)} )} 出现异常时直接返回 {@link Validator#getFailedMessage()}，否则将打印异常后返回。
     * </p>
     *
     * @return 是否忽略异常
     */
    @Override
    public boolean isIgnoreException() {
        return true;
    }

    /**
     * 验证器名称。此项不能为空。
     *
     * @return 验证器名称
     */
    @Override
    public @NonNull String getName() {
        return "ProductCheck";
    }

    /**
     * 验证失败时返回的信息。此项不能为空。
     *
     * <p>
     * 我们支持您根据不同的条件在此处返回不同的信息，但这种动态返回仅支持此方法与 {@link Validator#getServerVerifierData()} 方法。
     * 其余方法动态返回将是不安全且不推荐的做法。
     * </p>
     *
     * @return 验证失败时返回的信息
     */
    @Override
    public @NonNull String getFailedMessage() {
        return "PRODUCT_CHECK";
    }

    /**
     * 对于此验证器的描述。
     *
     * @return 描述
     */
    @Override
    public String getDescription() {
        return "产品检查";
    }

    /**
     * 服务器验证数据。
     *
     * <p>
     * 当您尝试修改了服务器验证数据时，这里必须为您修改后的对象，否则将不会有 IO 操作。
     * </p>
     *
     * <p>
     * 若为 {@code null} 则为未进行修改，若未空 {@code ""} 则未删除该用户的数据。
     * </p>
     *
     * @return 服务器验证数据
     */
    @Override
    public Object getServerVerifierData() {
        return null;
    }
}
