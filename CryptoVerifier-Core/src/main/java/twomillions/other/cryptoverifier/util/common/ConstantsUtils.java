package twomillions.other.cryptoverifier.util.common;

import lombok.experimental.UtilityClass;

/**
 * 常量类。
 *
 * <p>
 * 该类使用 {@link UtilityClass} 自动生成私有构造函数，并自动处理类、字段、内部类等。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/27
 */
@UtilityClass
@SuppressWarnings("unused")
public class ConstantsUtils {
    /**
     * 常用数据库通用名称。
     */
    public static final String FILE_DB_TYPE = "file";
    public static final String MONGO_DB_TYPE = "mongo";
    public static final String MONGODB_DB_TYPE = "mongodb";
    public static final String MYSQL_DB_TYPE = "mysql";

    /**
     * 常用数据库集合名称。
     */
    public static final String SERVER_DATA = "ServerData";

    /**
     * 常用类型名称。
     */
    public static final String SOCKET = "socket";
}
