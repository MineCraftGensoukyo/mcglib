package moe.gensoukyo.lib.util.cnpc.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.ai.EntityAIRangedAttack;
import noppes.npcs.api.entity.ICustomNpc;

import java.util.Objects;

/**
 * @author ChloePrime
 */
public final class CustomNpcRangedAiWrapper extends EntityAIRangedAttack {
    private final EntityAIBase delegate;
    private int mutexTemp = 0;
    private boolean mutexChangedInCtor = false;

    public
    CustomNpcRangedAiWrapper(EntityAIBase delegate, IRangedAttackMob mob) {
        super(mob);
        this.delegate = Objects.requireNonNull(delegate);

        if (mutexChangedInCtor) {
            delegate.setMutexBits(mutexTemp);
        }
    }

    public <E extends EntityCreature>
    CustomNpcRangedAiWrapper(NpcAiBase delegate, ICustomNpc<E> mob) {
        this(delegate.getMcAi(), CustomAiWrapper.checkedCast(mob.getMCEntity()));
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
        if (delegate != null) {
            delegate.setMutexBits(mutexBitsIn);
            return;
        }
        mutexTemp = mutexBitsIn;
        mutexChangedInCtor = true;
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
