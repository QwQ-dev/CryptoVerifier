package twomillions.other.cryptoverifier.commands.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;

/**
 * 已注册命令对象。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/27
 */
@Getter
@AllArgsConstructor
public class RegisteredCommand {
    /**
     * 实现 {@link Command} 的类。
     */
    private final Command command;
}
