package moe.gensoukyo.lib.timer;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author ChloePrime
 */
@SuppressWarnings("unused")
public interface ITask {
    /**
     * Run the callback after some ticks
     *
     * @param ticks    Delay amount (in game ticks)
     * @param callback called when time passed
     * @return the task handle, which can stop the task
     */
    ITaskItem delay(int ticks, @Nonnull Consumer<ITaskItem> callback);

    /**
     * Run the callback after some ticks and get the value,
     * using the {@link CompletableFuture} introduced in Java8
     *
     * @param ticks    Delay amount (in game ticks)
     * @param callback called when time passed
     * @param <R> Type of return value
     * @return a {@link CompletableFuture} that completes synchronized after the given delay
     */
    default <R> CompletableFuture<R> runDelayed(int ticks, @Nonnull Callable<R> callback) {
        CompletableFuture<R> future = new CompletableFuture<>();
        delay(ticks, ignored -> {
            R ret;
            try {
                ret = callback.call();
            } catch (Exception e) {
                future.completeExceptionally(e);
                return;
            }
            // Don't catch exception in thenXXX(code) added by callers
            future.complete(ret);
        });
        return future;
    }

    /**
     * Run the callback repeatedly with the given interval
     *
     * @param ticks    Delay amount (in game ticks)
     * @param callback called repeatedly
     * @return the task handle, which can stop the task
     */
    ITaskItem repeat(int ticks, @Nonnull Consumer<ITaskItem> callback);

    /**
     * Run the callback repeatedly with given times and given interval
     *
     * @param ticks    Delay amount (in game ticks)
     * @param count    Repeat Count
     * @param callback Called repeatedly with given times
     * @return the task handle, which can stop the task
     */
    ITaskItem countdown(int ticks, int count, @Nonnull Consumer<ITaskItem> callback);

    /**
     * Clear all scheduled tasks
     */
    void clear();

    /**
     * Whether the task object is empty (has no task items)
     * @return Whether the task object has no task items
     */
    boolean isEmpty();

    /**
     * Updates all tasks' countdown
     *
     * Tasks created by default constructor will update once a tick.
     * While some tasks may be updated manually.
     */
    void update();

    interface ITaskItem {
        /**
         * Stop this task,
         * avoids the callback being called furthermore.
         */
        void stop();
    }
}
