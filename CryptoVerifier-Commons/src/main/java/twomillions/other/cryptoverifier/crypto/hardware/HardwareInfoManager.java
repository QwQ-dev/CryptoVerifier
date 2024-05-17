package twomillions.other.cryptoverifier.crypto.hardware;

import lombok.experimental.UtilityClass;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * 用于获取机器硬件信息的实用类。
 *
 * <p>
 * 该类提供了一个静态方法，用于生成机器的唯一标识符。
 * 标识符由操作系统、处理器、磁盘、网络接口、电源、主板、BIOS 等硬件信息组合而成。
 * </p>
 *
 * <p>
 * 建议对该类进行混淆，以降低代码被逆向工程伪造 HWID 等风险，增强安全性。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/6/17
 */
@UtilityClass
public class HardwareInfoManager {
    /**
     * 获取机器的唯一标识符。
     *
     * @return 机器的唯一标识符，以 Base64 编码的字符串形式返回
     */
    public static String getMachineIdentifier() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();

        // 获取处理器信息
        CentralProcessor processor = hardware.getProcessor();
        String processorIdentifier = processor.getProcessorIdentifier().getIdentifier();
        String processorSerialNumber = processor.getProcessorIdentifier().getProcessorID();

        // 获取磁盘信息
        String diskSerialNumbers = hardware.getDiskStores().stream()
                .map(HWDiskStore::getSerial)
                .collect(Collectors.joining());

        // 获取网络接口信息
        String macAddresses = hardware.getNetworkIFs().stream()
                .map(NetworkIF::getMacaddr)
                .collect(Collectors.joining());

        // 获取电源信息
        String powerSourcesSerialNumbers = hardware.getPowerSources().stream()
                .map(PowerSource::getSerialNumber)
                .collect(Collectors.joining());

        // 获取主板信息
        ComputerSystem computerSystem = hardware.getComputerSystem();
        String baseboardManufacturer = computerSystem.getBaseboard().getManufacturer();
        String baseboardModel = computerSystem.getBaseboard().getModel();
        String baseboardSerialNumber = computerSystem.getBaseboard().getSerialNumber();

        // 获取 BIOS 信息
        String biosManufacturer = computerSystem.getManufacturer();
        String biosVersion = computerSystem.getFirmware().getVersion();

        // 组合硬件信息为特征码
        String hwidString = os + baseboardManufacturer + baseboardModel + baseboardSerialNumber +
                biosManufacturer + biosVersion + processorIdentifier + processorSerialNumber +
                diskSerialNumbers + macAddresses + powerSourcesSerialNumbers;

        return new String(Base64.getEncoder().encode(hwidString.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
