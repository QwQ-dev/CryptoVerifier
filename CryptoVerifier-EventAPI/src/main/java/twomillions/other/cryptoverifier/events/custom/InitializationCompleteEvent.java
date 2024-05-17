package twomillions.other.cryptoverifier.events.custom;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import twomillions.other.cryptoverifier.events.impl.interfaces.Event;

/**
 * 程序初始化完成事件。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
@NoArgsConstructor
@Accessors(chain = true)
public class InitializationCompleteEvent implements Event {
}
