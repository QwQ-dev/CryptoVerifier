package twomillions.other.cryptoverifier.thread;

import lombok.NonNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 该类是 {@link VerifierThreadPool} 使用的自定义 {@link ThreadFactory} 实现。
 *
 * <p>
 * 该类创建的线程名称为 {@code VerifierThread-X}，其中 {@code X} 是一个递增的数字。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/6/30
 */
public class VerifierThreadFactory implements ThreadFactory {
    /**
     * 线程名称的递增数字。
     */
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * 创建一个新的线程并更改名称。
     *
     * @param runnable 可运行的任务
     * @return 创建的线程
     */
    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName("VerifierThread-" + threadNumber.getAndIncrement());
        return thread;
    }
}
