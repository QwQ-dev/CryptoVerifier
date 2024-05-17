package twomillions.other.cryptoverifier.io.databases.enums.types;

import java.util.Arrays;

/**
 * @author 2000000
 */
@SuppressWarnings("unused")
public enum PersistentDataStorageType {

    /**
     * MongoDB - MongoDB 存储
     */
    MongoDB,

    /**
     * MySQL - MySQL 存储
     */
    MySQL,

    /**
     * File - File 存储
     */
    File;

    /**
     * valueOf 忽略大小写。
     *
     * @param name name
     * @return PersistentDataStorageType
     */
    public static PersistentDataStorageType valueOfIgnoreCase(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant " + PersistentDataStorageType.class.getCanonicalName() + "." + name));
    }
}
