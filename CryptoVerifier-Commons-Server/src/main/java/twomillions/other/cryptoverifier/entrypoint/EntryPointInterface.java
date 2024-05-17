package twomillions.other.cryptoverifier.entrypoint;

/**
 * 插件入口接口。
 *
 * <p>
 * 所有插件入口都必须实现此接口并重写方法。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
@SuppressWarnings("unused")
public interface EntryPointInterface {
    void onLoad();
}
