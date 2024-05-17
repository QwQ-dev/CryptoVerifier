package twomillions.other.cryptoverifier;

import lombok.NonNull;
import twomillions.other.cryptoverifier.crypto.verifier.data.ClientVerifierDataAPI;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierDataAPI;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

/**
 * 示例自定义验证器，应实现 {@link Validator} 重写所有方法。
 *
 * <p>
 * 此类将实现一个简易的自定义验证器作为示例。
 * </p>
 *
 * <p>
 * 此验证将对使用 IP 进行检查，若存在 {@code 127.0.0.1} 则进行删除并返回 {@code false}，忽略异常。
 * </p>
 *
 * @author 2000000
 * @version 1.1
 * @since 2023/7/14
 */
public class ExampleValidation implements Validator {
    /**
     * {@link ServerVerifierDataAPI} 对象。
     *
     * <p>
     * 私有成员变量，这里将其提取出来是因为在 {@link Validator#validate(String, CredentialData, Object, Object)} )} 方法中
     * 对 {@code serverVerifierDataObject} 进行了更改，需要通过 {@link ExampleValidation#getServerVerifierData()} 方法返回新数据，告知服务端更新数据。
     * </p>
     */
    private ServerVerifierDataAPI serverVerifierDataAPI;

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
        /*
         * 获取 ServerVerifierDataAPI 对象并赋值于私有成员变量 serverVerifierDataAPI
         * ServerVerifierDataAPI 提供了对 ServerVerifierData 对象特定属性的 getter 及 setter 方法
         *
         * ServerVerifierDataAPI 含有两个构造，分别传入 serverVerifierDataObject 或 serverVerifierDataString
         * 若通过 serverVerifierDataString 进行构造则需要 try-catch 捕获异常或抛出，在有 Object 的情况下我们不建议使用字符串进行构造
         */
        serverVerifierDataAPI = new ServerVerifierDataAPI(serverVerifierDataObject);

        /*
         * CredentialData 是凭证数据对象，可以在此对象内获取一些凭证信息
         *
         * String file = credentialData.getFile(); // 许可证文件内容
         * String hwid = credentialData.getHwid(); // Hwid
         * String fileName = credentialData.getFileName(); // 许可证文件名
         * String extraData = credentialData.getExtraData(); // 客户端额外数据
         */

        /*
         * 通过 getter 方法获取此许可证的有关信息
         * 我们并没有使用 serverVerificationInfo 变量，这里使用 SuppressWarnings 注解抑制该变量的未使用警告
         */
        @SuppressWarnings("unused")
        String serverVerificationInfo = serverVerifierDataAPI.getVerificationInfo();

        /*
         * ClientVerifierDataAPI 构造器与 ServerVerifierDataAPI 相同
         * ClientVerifierDataAPI 并不提供对应的 setter 方法，因为这些数据都存在于客户端文件中
         */
        ClientVerifierDataAPI clientVerifierDataAPI = new ClientVerifierDataAPI(clientVerifierDataObject);

        /*
         * 在使用 ClientVerifierDataAPI 的 getter 方法时，通常需要传入 ServerVerifierData 对象
         * 因为 ClientVerifierData 内容除了 UUID 均是非对称加密，其密钥均保存在对应的 ServerVerifierData 中
         *
         * 我们并没有使用 clientVerificationInfo 变量，这里使用 SuppressWarnings 注解抑制该变量的未使用警告
         */
        @SuppressWarnings("unused")
        String clientVerificationInfo = clientVerifierDataAPI.getVerificationInfo(serverVerifierDataAPI.getServerVerifierData());

        /*
         * 接下来实现一个简单的过滤
         * 如果许可证记录的使用过的 IP 中存在 127.0.0.1 则进行删除，并且不通过验证
         * 如果 clientIp 也是 127.0.0.1 那么也不通过验证
         *
         * 需要注意的是，我们对 serverVerifierDataObject 进行了更改，所以在 getServerVerifierData 方法需要返回我们更改后的对象
         */
        return !serverVerifierDataAPI.getIps().removeIf(ipaddress -> ipaddress.equals("127.0.0.1")) || !ip.equals("127.0.0.1");
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
        return "ExampleValidation";
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
        return "EXAMPLE_VALIDATION";
    }

    /**
     * 对于此验证器的描述。
     *
     * @return 描述
     */
    @Override
    public String getDescription() {
        return "一个简易的自定义验证器";
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
        return serverVerifierDataAPI.getServerVerifierData();
    }
}
