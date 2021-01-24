package moe.gensoukyo.lib.timer;

import com.google.common.base.Preconditions;
import moe.gensoukyo.lib.MCGLib;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Task impl with auto update possibility.
 * @author zat, Chloe_koopa
 */
@Mod.EventBusSubscriber(modid = MCGLib.MODID)
public class Task implements ITask {
    private final boolean autoUpdate;
    private final Side logicSide;
    private final List<TaskItem> items = new LinkedList<>();

    private static final List<WeakReference<Task>> AUTO_UPDATE_SERVER = new LinkedList<>();
    private static final List<WeakReference<Task>> AUTO_UPDATE_CLIENT = new LinkedList<>();

    private static List<WeakReference<Task>> getInstancesForSide(Side side) {
        return side == Side.CLIENT ? AUTO_UPDATE_CLIENT : AUTO_UPDATE_SERVER;
    }

    @SuppressWarnings("unused")
    public Task() {
        this(true);
    }

    public Task(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
        this.logicSide = FMLCommonHandler.instance().getEffectiveSide();

        if (!this.autoUpdate) {
            return;
        }

        getInstancesForSide(this.logicSide).add(new WeakReference<>(this));
    }

    @Override
    public TaskItem delay(int ticks, @Nonnull Consumer<TaskItem> callback) {
        Preconditions.checkNotNull(callback);
        return new TaskItem(ticks, callback, 1);
    }

    @Override
    public TaskItem repeat(int ticks, @Nonnull Consumer<TaskItem> callback) {
        Preconditions.checkNotNull(callback);
        return new TaskItem(ticks, callback, TaskItem.REPEAT);
    }

    @Override
    public TaskItem countdown(int sec, int count, @Nonnull Consumer<TaskItem> callback) {
        Preconditions.checkNotNull(callback);
        return new TaskItem(sec, callback, count);
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
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

        Iterator<WeakReference<Task>> ite = getInstancesForSide(currentSide).iterator();
        while (ite.hasNext()) {
            Task task = ite.next().get();
            if (task == null || !task.autoUpdate || !task.isOnRightSide(currentSide)) {
                ite.remove();
                continue;
            }
            task.update();
        }
    }

    private boolean isOnRightSide(Side sideIn) {
        return this.logicSide == sideIn;
    }

    @Override
    public void update() {
        Iterator<TaskItem> i = items.iterator();
        while (i.hasNext()) {
            TaskItem item = i.next();
            if (item.isTerminated()) {
                i.remove();
                continue;
            }
            item.update();
        }
    }

    public class TaskItem implements ITaskItem {
        private final int initialCountdown;
        private final Consumer<TaskItem> callback;
        /**
         * -2持续
         * -1
         */
        private int count;
        private int curCountdown;
        private static final int REPEAT = -1;

        /**
         * @param count -1时持续
         */
        private TaskItem(int sec, Consumer<TaskItem> callback, int count) {
            this.initialCountdown = sec;
            this.callback = callback;
            this.count = count;

            this.curCountdown = this.initialCountdown;
            items.add(this);
        }

        private void update() {
            --this.curCountdown;
            if ((this.curCountdown <= 0) && (!this.isTerminated())) {
                nextLoop();
            }
        }

        @Override
        public void stop() {
            this.count = 0;
        }

        private void nextLoop() {
            this.callback.accept(this);
            if (this.count == 0) {
                return;
            }
            this.curCountdown = this.initialCountdown;
            if (this.count > 0) {
                --this.count;
            }
        }

        private boolean isTerminated() {
            return this.count == 0;
        }
    }
}
