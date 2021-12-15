package moe.gensoukyo.lib.util.cnpc.ai;

import net.minecraft.entity.ai.EntityAIBase;

import java.util.Objects;

/**
 * @author ChloePrime
 */
public final class NpcAiByMcp extends NpcAiBase {

    public static NpcAiBase of(EntityAIBase mcp) {
        if (mcp instanceof CustomAiWrapper) {
            return ((CustomAiWrapper) mcp).delegate;
        }
        return new NpcAiByMcp(mcp);
    }

    private final EntityAIBase mcp;

    private NpcAiByMcp(EntityAIBase mcp) {
        this.mcp = Objects.requireNonNull(mcp);
    }

    @Override
    public boolean shouldExecute() {
        return mcp.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return mcp.shouldContinueExecuting();
    }

    @Override
    public boolean isInterruptable() {
        return mcp.isInterruptible();
    }

    @Override
    public void startExecuting() {
        mcp.startExecuting();
    }

    @Override
    public void resetTask() {
        mcp.resetTask();
    }

    @Override
    public void updateTask() {
        mcp.updateTask();
    }

    @Override
    public void setMutex(int mutexBitsIn) {
        mcp.setMutexBits(mutexBitsIn);
    }

    @Override
    public int getMutex() {
        return mcp.getMutexBits();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NpcAiByMcp that = (NpcAiByMcp) o;
        return mcp.equals(that.mcp);
    }

    @Override
    public int hashCode() {
        return mcp.hashCode();
    }
}
