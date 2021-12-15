package moe.gensoukyo.lib.util.cnpc.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.ai.EntityAIAttackTarget;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

/**
 * @author ChloePrime
 */
public final class CustomNpcMeleeAiWrapper extends EntityAIAttackTarget {
    private final EntityAIBase delegate;

    public
    CustomNpcMeleeAiWrapper(EntityAIBase delegate, EntityNPCInterface npc) {
        super(npc);
        this.delegate = delegate;
    }

    public <E extends EntityCreature>
    CustomNpcMeleeAiWrapper(NpcAiBase delegate, ICustomNpc<E> npc) {
        this(delegate.getMcAi(), CustomAiWrapper.checkedCast(npc.getMCEntity()));
    }

    @Override
    public boolean shouldExecute() {
        return delegate.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return delegate.shouldContinueExecuting();
    }

    @Override
    public boolean isInterruptible() {
        return delegate.isInterruptible();
    }

    @Override
    public void startExecuting() {
        delegate.startExecuting();
    }

    @Override
    public void resetTask() {
        delegate.resetTask();
    }

    @Override
    public void updateTask() {
        delegate.updateTask();
    }

    @Override
    public void setMutexBits(int mutexBitsIn) {
        delegate.setMutexBits(mutexBitsIn);
    }

    @Override
    public int getMutexBits() {
        return delegate.getMutexBits();
    }

    @Override
    public void navOverride(boolean nav) {
        if (delegate instanceof NavOverrideable) {
            ((NavOverrideable) delegate).navOverride(nav);
        } else {
            super.navOverride(nav);
        }
    }
}
