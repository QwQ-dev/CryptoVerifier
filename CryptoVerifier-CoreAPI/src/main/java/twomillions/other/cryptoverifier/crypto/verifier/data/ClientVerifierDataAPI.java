package twomillions.other.cryptoverifier.crypto.verifier.data;

/**
 * ClientVerifierDataAPI.
 *
 * <p>
 * 该 API 类提供了对 {@link ClientVerifierData} 对象特定属性的 getter 方法。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/10
 */
@SuppressWarnings("unused")
public class ClientVerifierDataAPI {
    /**
     * {@link ClientVerifierData} 对象。
     */
    private final ClientVerifierData clientVerifierData;

    /**
     * 通过 {@link ClientVerifierData} 对象构造。
     *
     * @param clientVerifierDataObject {@link ClientVerifierData} 对象
     */
    public ClientVerifierDataAPI(Object clientVerifierDataObject) {
        clientVerifierData = ((ClientVerifierData) clientVerifierDataObject);
    }

    /**
     * 通过 {@link ClientVerifierData} 对象字符串构造。
     *
     * @param clientVerifierDataString {@link ClientVerifierData} 对象字符串
     * @throws Exception 如果转换时出现异常
     */
    public ClientVerifierDataAPI(String clientVerifierDataString) throws Exception {
        clientVerifierData = ClientVerifierData.fromString(clientVerifierDataString);
    }

    /**
     * 获取 Uuid.
     *
     * @return Uuid
     * @throws Exception 如果解密过程中发生异常
     */
    public String getUuid() throws Exception {
        return clientVerifierData.getUuid();
    }

    /**
     * 获取客户端文件内写入的许可证创建时间。
     *
     * @return 创建时间
     * @throws Exception 如果解密过程中发生异常
     */
    public String getCreationDate(Object serverVerifierData) throws Exception {
        return clientVerifierData.getCreationDate(((ServerVerifierData) serverVerifierData));
    }

    /**
     * 获取客户端文件内写入的许可证验证信息。
     *
     * @return 验证信息
     * @throws Exception 如果解密过程中发生异常
     */
    public String getVerificationInfo(Object serverVerifierData) throws Exception {
        return clientVerifierData.getVerificationInfo(((ServerVerifierData) serverVerifierData));
    }
}
