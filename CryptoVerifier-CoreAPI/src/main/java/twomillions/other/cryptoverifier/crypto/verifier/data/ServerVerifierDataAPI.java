package twomillions.other.cryptoverifier.crypto.verifier.data;

import twomillions.other.cryptoverifier.io.databases.DataManager;

import java.util.Set;

/**
 * ServerVerifierDataAPI.
 *
 * <p>
 * 该 API 类提供了对 {@link ServerVerifierData} 对象特定属性的 getter 及 setter 方法。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/10
 */
@SuppressWarnings("unused")
public class ServerVerifierDataAPI {
    /**
     * {@link ServerVerifierData} 对象。
     */
    private final ServerVerifierData serverVerifierData;

    /**
     * 通过 {@link ServerVerifierData} 对象构造。
     *
     * @param serverVerifierDataObject {@link ServerVerifierData} 对象
     */
    public ServerVerifierDataAPI(Object serverVerifierDataObject) {
        serverVerifierData = ((ServerVerifierData) serverVerifierDataObject);
    }

    /**
     * 通过 {@link ServerVerifierData} 对象字符串构造。
     *
     * @param serverVerifierDataString {@link ServerVerifierData} 对象字符串
     * @throws Exception 如果转换时出现异常
     */
    public ServerVerifierDataAPI(String serverVerifierDataString) throws Exception {
        serverVerifierData = ServerVerifierData.fromString(serverVerifierDataString);
    }

    /**
     * 获取 {@link ServerVerifierData} 对象。
     *
     * <p>
     * 为了安全起见，您应使用 {@link ServerVerifierDataAPI#toString()} 获取 {@link ServerVerifierData} 字符串对象。
     * </p>
     *
     * @return {@link ServerVerifierData} 对象
     */
    public Object getServerVerifierData() {
        return serverVerifierData;
    }

    /**
     * 将此处的 {@link ServerVerifierDataAPI#serverVerifierData} 对象转换为字符串。
     *
     * <p>
     * 为了安全起见，您应该使用此处的方法，而不是 {@link ServerVerifierDataAPI#getServerVerifierData()#toString()} 方法。
     * </p>
     *
     * @return {@link ServerVerifierData} 字符串对象
     */
    public String toString() {
        return serverVerifierData.toString();
    }

    /**
     * 保存目前的数据。
     *
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI saveData() {
        DataManager.save(getUuid(), toString());
        return this;
    }

    /**
     * 获取 Uuid.
     *
     * @return Uuid
     */
    public String getUuid() {
        return serverVerifierData.getUuid();
    }

    /**
     * 获取上次使用时间。
     *
     * @return 上次使用时间
     */
    public String getLastUsedTime() {
        return serverVerifierData.getLastUsedTime();
    }

    /**
     * 检查是否临时封禁。
     *
     * @return 如果临时封禁返回 true，否则返回 false
     */
    public boolean isTempBanned() {
        return serverVerifierData.isTempBanned();
    }

    /**
     * 获取临时封禁信息。
     *
     * @return 临时封禁信息
     */
    public String getTempBannedInfo() {
        return serverVerifierData.getTempBannedInfo();
    }

    /**
     * 获取 Hwid 限制。
     *
     * @return Hwid 限制
     */
    public int getHwidLimit() {
        return serverVerifierData.getHwidLimit();
    }

    /**
     * 获取 Hwid 集合。
     *
     * @return Hwid 集合
     */
    public Set<String> getHwids() {
        return serverVerifierData.getHwids();
    }

    /**
     * 获取 IP 限制。
     *
     * @return IP 限制
     */
    public int getIpLimit() {
        return serverVerifierData.getIpLimit();
    }

    /**
     * 获取 IP 集合。
     *
     * @return IP 集合
     */
    public Set<String> getIps() {
        return serverVerifierData.getIps();
    }

    /**
     * 获取验证次数限制。
     *
     * @return 验证次数限制
     */
    public int getVerificationLimit() {
        return serverVerifierData.getVerificationLimit();
    }

    /**
     * 获取验证次数。
     *
     * @return 验证次数
     */
    public int getVerificationTimes() {
        return serverVerifierData.getVerificationTimes();
    }

    /**
     * 获取并行限制。
     *
     * @return 并行限制
     */
    public int getParallelLimit() {
        return serverVerifierData.getParallelLimit();
    }

    /**
     * 获取创建日期。
     *
     * @return 创建日期
     */
    public String getCreationDate() {
        return serverVerifierData.getCreationDate();
    }

    /**
     * 获取文件名。
     *
     * @return 文件名
     */
    public String getFileName() {
        return serverVerifierData.getFileName();
    }

    /**
     * 获取验证信息。
     *
     * @return 验证信息
     */
    public String getVerificationInfo() {
        return serverVerifierData.getVerificationInfo();
    }

    /**
     * 获取过期日期。
     *
     * @return 过期日期
     */
    public String getExpirationDate() {
        return serverVerifierData.getExpirationDate();
    }

    /**
     * 获取自定义数据。
     *
     * @return 自定义数据
     */
    public String getCustomData() {
        return serverVerifierData.getCustomData();
    }

    /**
     * 设置 Uuid.
     *
     * @param uuid Uuid
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setUuid(String uuid) {
        serverVerifierData.setUuid(uuid);
        return this;
    }

    /**
     * 设置上次使用时间。
     *
     * @param lastUsedTime 上次使用时间
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setLastUsedTime(String lastUsedTime) {
        serverVerifierData.setLastUsedTime(lastUsedTime);
        return this;
    }

    /**
     * 设置临时封禁状态。
     *
     * @param tempBanned 是否临时封禁
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setTempBanned(boolean tempBanned) {
        serverVerifierData.setTempBanned(tempBanned);
        return this;
    }

    /**
     * 设置临时封禁信息。
     *
     * @param tempBannedInfo 临时封禁信息
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setTempBannedInfo(String tempBannedInfo) {
        serverVerifierData.setTempBannedInfo(tempBannedInfo);
        return this;
    }

    /**
     * 设置 Hwid 限制。
     *
     * @param hwidLimit Hwid 限制
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setHwidLimit(int hwidLimit) {
        serverVerifierData.setHwidLimit(hwidLimit);
        return this;
    }

    /**
     * 设置 Hwid 集合。
     *
     * @param hwids Hwid 集合
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setHwids(Set<String> hwids) {
        serverVerifierData.setHwids(hwids);
        return this;
    }

    /**
     * 设置 IP 限制。
     *
     * @param ipLimit IP 限制
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setIpLimit(int ipLimit) {
        serverVerifierData.setIpLimit(ipLimit);
        return this;
    }

    /**
     * 设置 IP 集合。
     *
     * @param ips IP 集合
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setIps(Set<String> ips) {
        serverVerifierData.setIps(ips);
        return this;
    }

    /**
     * 设置验证次数限制。
     *
     * @param verificationLimit 验证次数限制
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setVerificationLimit(int verificationLimit) {
        serverVerifierData.setVerificationLimit(verificationLimit);
        return this;
    }

    /**
     * 设置验证次数。
     *
     * @param verificationTimes 验证次数
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setVerificationTimes(int verificationTimes) {
        serverVerifierData.setVerificationTimes(verificationTimes);
        return this;
    }

    /**
     * 设置并行限制。
     *
     * @param parallelLimit 并行限制
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setParallelLimit(int parallelLimit) {
        serverVerifierData.setParallelLimit(parallelLimit);
        return this;
    }

    /**
     * 设置创建日期。
     *
     * @param creationDate 创建日期
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setCreationDate(String creationDate) {
        serverVerifierData.setCreationDate(creationDate);
        return this;
    }

    /**
     * 设置文件名。
     *
     * @param fileName 文件名
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setFileName(String fileName) {
        serverVerifierData.setFileName(fileName);
        return this;
    }

    /**
     * 设置验证信息。
     *
     * @param verificationInfo 验证信息
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setVerificationInfo(String verificationInfo) {
        serverVerifierData.setVerificationInfo(verificationInfo);
        return this;
    }

    /**
     * 设置过期日期。
     *
     * @param expirationDate 过期日期
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setExpirationDate(String expirationDate) {
        serverVerifierData.setExpirationDate(expirationDate);
        return this;
    }

    /**
     * 设置自定义数据。
     *
     * @param customData 自定义数据
     * @return 当前的 {@link ServerVerifierDataAPI} 对象
     */
    public ServerVerifierDataAPI setCustomData(String customData) {
        serverVerifierData.setCustomData(customData);
        return this;
    }
}
