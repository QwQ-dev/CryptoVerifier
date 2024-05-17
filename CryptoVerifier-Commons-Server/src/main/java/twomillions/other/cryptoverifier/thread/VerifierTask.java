package twomillions.other.cryptoverifier.thread;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Future;

/**
 * 该类表示由 {@link VerifierThreadPool} 执行的任务，持有与任务关联的 {@link Future} 对象的引用，允许取消任务的执行。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/6/30
 */
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class VerifierTask {
    /**
     * 与任务关联的 Future 对象。
     */
    private final Future<?> future;

    /**
     * 取消任务的执行。
     */
    public void stop() {
        future.cancel(true);
    }
}