package twomillions.other.cryptoverifier.validators.impl.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 验证结果的返回对象。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/14
 */
@Getter
@Setter
@Accessors(chain = true)
public class ValidatorResult {
    /**
     * 验证结果。
     */
    private String resultString;

    /**
     * 服务器验证数据对象。
     */
    private Object serverVerifierData;
}
