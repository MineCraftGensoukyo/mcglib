package moe.gensoukyo.lib.util.cnpc.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.entity.EntityNPCInterface;

/**
 * EntityAIBase that wraps a scriptable AI.
 *
 * @author ChloePrime
 * @see NpcAiBase
 */
final class CustomAiWrapper extends EntityAIBase {

    /**
     * Helper method for casting ICustomNpc<N>'s N to EntityNpcInterface
     */
    @SuppressWarnings("unchecked")
    static <N extends EntityNPCInterface>
    N checkedCast(Entity obj) {
        EntityNPCInterface npc = (EntityNPCInterface) obj;
        return (N) npc;
    }

    final NpcAiBase delegate;

    CustomAiWrapper(NpcAiBase delegate) {
        this.delegate = delegate;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        return delegate.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return delegate.shouldContinueExecuting();
    }

    /**
     * Determine if this AI Task is interruptable by a higher (= lower value) priority task. All vanilla AITask have
     * this value set to true.
     */
    @Override
    public boolean isInterruptible() {
        return delegate.isInterruptable();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        delegate.startExecuting();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        delegate.resetTask();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void updateTask() {
        delegate.updateTask();
    }

    /**
     * Sets the mutex bitflags, see getMutexBits. Flag 1 for motion, flag 2 for look/head movement, flag 4 for
     * swimming/misc. Flags can be OR'ed.
     */
    @Override
    public void setMutexBits(int mutexBitsIn) {
        delegate.setMutex(mutexBitsIn);
    }

    /**
     * Get what actions this task will take that may potentially conflict with other tasks. The test is a simple bitwise
     * AND - if it yields zero, the two tasks may run concurrently, if not - they must run exclusively from each other.
     * See setMutexBits.
     */
    @Override
    public int getMutexBits() {
        return delegate.getMutex();
    }

    @Override
    public String toString() {
        return "CustomAiWrapper{" +
                "delegate=" + delegate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomAiWrapper that = (CustomAiWrapper) o;
        return delegate.equals(that.delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

}
