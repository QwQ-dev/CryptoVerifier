package twomillions.other.cryptoverifier.validators.impl.interfaces;

import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.validators.impl.objects.ValidatorResult;

/**
 * 接口类。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
public interface ValidatorAPI {
    /**
     * 注册验证器。
     *
     * @param validator {@link Validator}
     */
    void registerValidator(Validator validator);

    /**
     * 调用验证器进行验证。
     *
     * @param ip                              客户端 IP 地址
     * @param credentialData                  凭证数据对象
     * @param clientVerifierDataObject        客户端验证数据对象
     * @param serverVerifierDataObject        服务器验证数据对象
     * @return 验证结果
     */
    ValidatorResult callValidators(String ip, CredentialData credentialData, Object clientVerifierDataObject
            , Object serverVerifierDataObject) throws Exception;
}

