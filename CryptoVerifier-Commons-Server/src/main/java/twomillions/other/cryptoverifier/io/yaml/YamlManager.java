package twomillions.other.cryptoverifier.io.yaml;

import de.leonhard.storage.SimplixBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import lombok.Getter;

import java.io.File;

/**
 * 该类提供对 Yaml 文件的操作，并允许获取配置文件。
 *
 * <p>
 * 通过使用 {@code @Getter} 注解，提供用于获配置文件的方法。
 * </p>
 *
 * <p>
 * 您可以使用该类获取到配置文件对象并进行读写操作，但对于部分内容的读取程序初始化时便会保存在内存中，即使修改后也不会重新读取。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @see <a href="https://github.com/Simplix-Softworks/SimplixStorage">Simplix Storage</a>
 * @since 2023/7/16
 */
public class YamlManager {
    /**
     * 配置文件对象。
     */
    @Getter
    private static final Yaml config = createYaml("config", "configs", true);

    /**
     * Yaml 文件后缀。
     */
    private static final String YAML_FILE_EXTENSION = ".yml";

    /**
     * 创建一个 {@link Yaml} 对象。
     *
     * @param fileName 文件名
     * @param path 文件路径
     * @param inputStreamFromResource 是否从资源内获取输入流
     * @return {@link Yaml} 对象
     */
    public static Yaml createYaml(String fileName, String path, boolean inputStreamFromResource) {
        if (fileName.contains(YAML_FILE_EXTENSION)) {
            fileName = fileName.split(YAML_FILE_EXTENSION)[0];
        }

        File file = new File(path, fileName + YAML_FILE_EXTENSION);

        if (file.exists() && inputStreamFromResource) {
            inputStreamFromResource = false;
        }

        SimplixBuilder simplixBuilder = SimplixBuilder.fromFile(file)
                .setDataType(DataType.SORTED)
                .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
                .setReloadSettings(ReloadSettings.MANUALLY);

        if (inputStreamFromResource) {
            simplixBuilder.addInputStreamFromResource(fileName + YAML_FILE_EXTENSION);
        }

        return simplixBuilder.createYaml();
    }
}
