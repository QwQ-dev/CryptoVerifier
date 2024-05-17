package twomillions.other.cryptoverifier.io.databases.enums.types;

import java.util.Arrays;

/**
 * @author 2000000
 */
@SuppressWarnings("unused")
public enum NonPersistentDataStorageType {

    /**
     * 无
     */
    None,

    /**
     * Redis - Redis 存储
     */
    Redis;

    /**
     * valueOf 忽略大小写。
     *
     * @param name name
     * @return PersistentDataStorageType
     */
    public static NonPersistentDataStorageType valueOfIgnoreCase(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant " + NonPersistentDataStorageType.class.getCanonicalName() + "." + name));
    }
}
