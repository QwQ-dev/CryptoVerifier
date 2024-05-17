package twomillions.other.cryptoverifier.thread;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.*;

/**
 * 线程池，提供执行任务的能力，并管理与任务关联的线程。
 *
 * <p>
 * 一般需通过静态方法 {@link VerifierThreadPool#getVerifierThreadPool()} 获取实例。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/6/30
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class VerifierThreadPool {
    /**
     * 获取 {@code CryptoVerifier} 使用的线程池实例。
     */
    @Getter
    @Setter
    private static VerifierThreadPool verifierThreadPool;

    /**
     * 线程池的执行器服务。
     */
    @Getter
    private final ExecutorService executorService;

    /**
     * 创建一个新的实例。
     *
     * @param corePoolSize  核心线程数
     * @param maxPoolSize   最大线程数
     * @param keepAliveTime 非核心线程的空闲时间
     * @param queueCapacity 任务队列的容量
     */
    public VerifierThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime, int queueCapacity) {
        executorService = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new VerifierThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 同步运行任务。
     *
     * @param task 需要执行的任务
     */
    public void runSync(Runnable task) {
        task.run();
    }

    /**
     * 异步运行任务。
     *
     * @param task 需要执行的任务
     * @return 与任务关联的 {@link VerifierTask} 对象
     */
    public VerifierTask runAsync(Runnable task) {
        return new VerifierTask(executorService.submit(task));
    }

    /**
     * 延迟异步运行任务。
     *
     * @param task        需要执行的任务
     * @param delayMillis 毫秒为单位的延迟执行时间
     * @return 与任务关联的 {@link VerifierTask} 对象
     */
    public VerifierTask runLaterAsync(Runnable task, long delayMillis) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new VerifierThreadFactory());
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.schedule(
                Executors.callable(task),
                delayMillis,
                TimeUnit.MILLISECONDS
        );
        scheduledExecutorService.shutdown();
        return new VerifierTask(scheduledFuture);
    }

    /**
     * 定时异步运行任务。
     *
     * @param task           需要执行的任务
     * @param delayMillis    毫秒为单位的延迟执行时间
     * @param intervalMillis 毫秒为单位的任务执行间隔
     * @return 与任务关联的 {@link VerifierTask} 对象
     */
    public VerifierTask runTimeAsync(Runnable task, long delayMillis, long intervalMillis) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new VerifierThreadFactory());
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(
                task,
                delayMillis,
                intervalMillis,
                TimeUnit.MILLISECONDS
        );
        return new VerifierTask(scheduledFuture);
    }

    /**
     * 关闭线程池。
     *
     * <p>
     * 当线程池关闭后，不再接受新的任务，并尝试停止正在执行的任务。
     * </p>
     */
    public void shutdown() {
        executorService.shutdown();
    }
}