package twomillions.other.cryptoverifier.events.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import twomillions.other.cryptoverifier.events.impl.interfaces.Event;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Socket 许可证验证完成事件。
 *
 * <p>
 * 通过使用 {@code @Getter} 和 {@code @Accessors(chain = true)} 注解，提供用于获取属性的方法，支持链式调用。
 * </p>
 *
 * <p>
 * 请注意，如果您使用此事件向客户端发送信息是极度不安全的，数据会直接发送，并不会加密。
 * </p>
 *
 * <p>
 * 如果您需要额外发送数据，我们推荐您使用 {@link LicenseValidationCompleteEvent} 事件并使用 {@link LicenseValidationCompleteEvent#setResult(String)} 追加返回信息，
 * 这样做是加密的。我们非常不建议您使用该事件直接向客户端发送未加密的数据，除非您是有意而为之。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/10
 */
@Getter
@AllArgsConstructor
@Accessors(chain = true)
public class SocketVerificationCompleteEvent implements Event {
    /**
     * Socket 对象，无需关闭。
     */
    private final Socket socket;

    /**
     * BufferedReader 对象，无需关闭。
     */
    private final BufferedReader bufferedReader;

    /**
     * PrintWriter 对象，无需关闭。
     */
    private final PrintWriter printWriter;
}

