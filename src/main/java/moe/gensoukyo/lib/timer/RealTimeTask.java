package moe.gensoukyo.lib.timer;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Task impl based on {@link System#currentTimeMillis}
 * @author ChloePrime
 */
public class RealTimeTask extends AbstractTask {
    public RealTimeTask() {
        super(true);
    }

    /**
     * 以系统时间作为当前时间。
     *
     * @return 系统时间，以毫秒为单位
     */
    @Override
    protected long getTickCount() {
        return System.currentTimeMillis();
    }

    @Override
    protected TaskItem newTaskItem(int delay, @Nonnull Consumer<ITaskItem> callback, int count) {
        // 将 sec 减少半tick，以防止由于毫秒数和tick数*50相差甚小，导致延时容易浮动1tick。
        return new TaskItem(getTickCount() - 25, delay, callback, count);
    }
}
