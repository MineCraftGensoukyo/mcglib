package moe.gensoukyo.lib.timer;

import com.google.common.base.Preconditions;
import moe.gensoukyo.lib.MCGLib;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;

/**
 * Task impl with auto update possibility.
 * @author zat, ChloePrime
 */
@Mod.EventBusSubscriber(modid = MCGLib.MODID)
public abstract class AbstractTask implements ITask {
    private static long getTimeOfArrival(TaskItem item) {
        return item.timeOfArrival;
    }
    private static final Logger LOGGER = LogManager.getLogger("MCGLib Tasks");

    private final boolean autoUpdate;
    private final Side logicSide;
    private final PriorityQueue<TaskItem> items = new PriorityQueue<>(
            Comparator.comparingLong(AbstractTask::getTimeOfArrival)
    );

    @SuppressWarnings("unused")
    public AbstractTask() {
        this(true);
    }

    public AbstractTask(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
        this.logicSide = FMLCommonHandler.instance().getEffectiveSide();

        if (!this.autoUpdate) {
            return;
        }

        getInstancesForSide(this.logicSide).add(new WeakReference<>(this));
    }

    @Override
    public TaskItem delay(int ticks, @Nonnull Consumer<ITaskItem> callback) {
        Preconditions.checkNotNull(callback);
        return newTaskItem(ticks, callback, 1);
    }

    @Override
    public TaskItem repeat(int delay, @Nonnull Consumer<ITaskItem> callback) {
        Preconditions.checkNotNull(callback);
        return newTaskItem(delay, callback, TaskItem.REPEAT);
    }

    @Override
    public TaskItem countdown(int delay, int count, @Nonnull Consumer<ITaskItem> callback) {
        Preconditions.checkNotNull(callback);
        return newTaskItem(delay, callback, count);
    }

    protected TaskItem newTaskItem(int delay, @Nonnull Consumer<ITaskItem> callback, int count) {
        return new TaskItem(getTickCount(), delay, callback, count);
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void update() {
        while (true) {
            TaskItem top = items.peek();
            if (top == null || top.timeOfArrival > getTickCount()) {
                return;
            }

            if (items.poll() != top) {
                throw new ConcurrentModificationException("getTickCount() changed the TaskItem list");
            }
            top.nextLoop();
            if (!top.isTerminated()) {
                items.offer(top);
            }
        }
    }

    /**
     * 获取当前时间。
     * 此处时间是个抽象的概念，可以用GameTick实现，也可以用真实时间/手动更新次数等实现。
     * @return 当前时间
     */
    protected abstract long getTickCount();

    public class TaskItem implements ITaskItem {
        private final int delay;
        private final Consumer<ITaskItem> callback;
        /**
         * -2持续
         * -1
         */
        private int count;
        private long timeOfArrival;
        private static final int REPEAT = -1;

        /**
         * @param count -1时持续
         */
        protected TaskItem(long startTime, int delay, Consumer<ITaskItem> callback, int count) {
            this.delay = delay;
            this.callback = callback;
            this.count = count;
            timeOfArrival = startTime + delay;

            items.add(this);
        }

        @Override
        public void stop() {
            this.count = 0;
        }

        private void nextLoop() {
            runCallback();
            if (this.count == 0) {
                return;
            }
            timeOfArrival += delay;
            if (this.count > 0) {
                --this.count;
            }
        }

        /**
         * 执行回调，如果遇到报错，则输出到日志。
         */
        private void runCallback() {
            try {
                this.callback.accept(this);
            } catch (OutOfMemoryError e) {
                throw e;
            } catch (Throwable e) {
                LOGGER.error("A task generated an exception", e);
                stop();
            }
        }

        private boolean isTerminated() {
            return this.count == 0;
        }
    }

    /* 每刻自动更新 */

    private static final List<WeakReference<AbstractTask>> AUTO_UPDATE_SERVER = new LinkedList<>();
    private static final List<WeakReference<AbstractTask>> AUTO_UPDATE_CLIENT = new LinkedList<>();

    private static List<WeakReference<AbstractTask>> getInstancesForSide(Side side) {
        return side == Side.CLIENT ? AUTO_UPDATE_CLIENT : AUTO_UPDATE_SERVER;
    }

    private boolean isOnRightSide(Side sideIn) {
        return this.logicSide == sideIn;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        onTick(event, Side.SERVER);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        onTick(event, Side.CLIENT);
    }

    private static void onTick(TickEvent event, Side currentSide) {
        if (event.phase == TickEvent.Phase.END) {
            return;
        }

        Iterator<WeakReference<AbstractTask>> ite = getInstancesForSide(currentSide).iterator();
        while (ite.hasNext()) {
            AbstractTask task = ite.next().get();
            if (task == null || !task.autoUpdate || !task.isOnRightSide(currentSide)) {
                ite.remove();
                continue;
            }
            task.update();
        }
    }
}
