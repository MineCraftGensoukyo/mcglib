package moe.gensoukyo.lib.util.cnpc.ai;

import kotlin.jvm.functions.Function1;
import moe.gensoukyo.lib.util.cnpc.apiext.noppes.npcs.api.entity.IEntityLivingKt;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntityLiving;

/**
 * Scriptable {@link EntityAIBase} that won't be obfuscated to MCP Naming.
 *
 * @see IEntityLivingKt#addTask(IEntityLiving, int, NpcAiBase)
 * @see CnpcAiKt#overrideBattleAi(ICustomNpc, Function1)
 * @author ChloePrime
 */
public abstract class NpcAiBase {
    /**
     * EntityAIBase that wraps this AI task.
     */
    private final CustomAiWrapper wrapped;

    protected NpcAiBase() {
        wrapped = new CustomAiWrapper(this);
    }

    /**
     * A bitmask telling which other tasks may not run concurrently. The test is a simple bitwise AND - if it yields
     * zero, the two tasks may run concurrently, if not - they must run exclusively from each other.
     */
    private int mutexBits;

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    /**
     * Determine if this AI Task is interruptable by a higher (= lower value) priority task. All vanilla AITask have
     * this value set to true.
     */
    public boolean isInterruptable() {
        return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask() {
    }

    /**
     * Sets the mutex bitflags, see getMutexBits. Flag 1 for motion, flag 2 for look/head movement, flag 4 for
     * swimming/misc. Flags can be OR'ed.
     */
    public void setMutex(int mutexBitsIn) {
        this.mutexBits = mutexBitsIn;
    }

    /**
     * Get what actions this task will take that may potentially conflict with other tasks. The test is a simple bitwise
     * AND - if it yields zero, the two tasks may run concurrently, if not - they must run exclusively from each other.
     * See setMutexBits.
     */
    public int getMutex() {
        return this.mutexBits;
    }

    /**
     * Wrap this AI object to vanilla Minecraft's AI class.
     */
    public final EntityAIBase getMcAi() {
        return wrapped;
    }
}
