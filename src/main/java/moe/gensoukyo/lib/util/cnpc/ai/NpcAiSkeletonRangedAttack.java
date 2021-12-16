package moe.gensoukyo.lib.util.cnpc.ai;

import kotlin.jvm.functions.Function0;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.data.INPCRanged;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.entity.EntityNPCInterface;

/**
 * Vanilla skeleton's attack ai, made for npc.
 *
 * @author Mojang, ChloePrime
 * @see EntityAIAttackRangedBow
 * @see CnpcAiKt#replaceRangedAi(ICustomNpc, Function0)
 */
@SuppressWarnings("unused")
public class NpcAiSkeletonRangedAttack extends NpcAiBase {

    private final EntityNPCInterface entity;
    private final double moveSpeedAmp;
    private final double farSq;
    private final double nearSq;
    private final float changeTangentProbability;
    private final float changeNormalProbability;

    public static final double VANILLA_FAR = Math.sqrt(0.75);
    public static final double VANILLA_NEAR = 0.5;
    public static final float VANILLA_TANGENT_SWAP_RATE = 0.3F;
    public static final float VANILLA_NORMAL_SWAP_RATE = 0.3F;

    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    private boolean active = false;

    public NpcAiSkeletonRangedAttack(ICustomNpc<?> npc,
                                     double moveSpeedAmpIn
    ) {
        this(CustomAiWrapper.checkedCast(npc.getMCEntity()), moveSpeedAmpIn);
    }

    public NpcAiSkeletonRangedAttack(EntityNPCInterface npc,
                                     double moveSpeedAmpIn
    ) {
        this(npc, moveSpeedAmpIn, VANILLA_FAR, VANILLA_NEAR, VANILLA_TANGENT_SWAP_RATE, VANILLA_NORMAL_SWAP_RATE);
    }

    private NpcAiSkeletonRangedAttack(EntityNPCInterface npc, double moveSpeedAmpIn,
                              double far, double near,
                              float tangentSwapRate, float normalSwapRate
    ) {
        this.entity = npc;
        this.moveSpeedAmp = moveSpeedAmpIn;
        this.farSq = far * far;
        this.nearSq = near * near;
        this.changeTangentProbability = tangentSwapRate;
        this.changeNormalProbability = normalSwapRate;
    }

    public double getMaxAttackDistance() {
        int range = entity.stats.ranged.getRange();
        return range * range;
    }

    public double getMaxStrafeDistanceSquared() {
        return getMaxAttackDistance() * farSq;
    }

    public double getMinStrafeDistanceSquared() {
        return getMaxAttackDistance() * nearSq;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = this.entity.getAttackTarget();
        return target != null && target.isEntityAlive() && this.hasProjectile();
    }

    protected boolean hasProjectile() {
        ItemStack proj = ItemStackWrapper.MCItem(this.entity.inventory.getProjectile());
        return proj != null && !proj.isEmpty();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.hasProjectile();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        super.startExecuting();
        this.entity.setSwingingArms(true);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.setSwingingArms(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.strafingTime = 0;
        this.deactivate();
    }

    /**
     * Keep ticking a continuous task that has already been started
     * <p>
     * Magic values are from Mojang :(
     */
    @Override
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public void updateTask() {
        EntityLivingBase entitylivingbase = this.entity.getAttackTarget();

        if (entitylivingbase == null) {
            return;
        }

        double distanceSq = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        boolean canSee = !this.entity.ais.directLOS || this.entity.getEntitySenses().canSee(entitylivingbase);
        boolean flag1 = this.seeTime > 0;

        if (canSee != flag1) {
            this.seeTime = 0;
        }

        if (canSee) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }

        if (distanceSq <= this.getMaxAttackDistance() && this.seeTime >= 20) {
            this.entity.getNavigator().clearPath();
            ++this.strafingTime;
        } else {
            this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
            this.strafingTime = -1;
        }

        if (this.strafingTime >= 20) {
            if (this.entity.getRNG().nextFloat() < changeTangentProbability) {
                this.strafingClockwise = !this.strafingClockwise;
            }

            if (this.entity.getRNG().nextFloat() < changeNormalProbability) {
                this.strafingBackwards = !this.strafingBackwards;
            }

            this.strafingTime = 0;
        }

        if (this.strafingTime > -1) {
            if (distanceSq > getMaxStrafeDistanceSquared()) {
                this.strafingBackwards = false;
            } else if (distanceSq < getMinStrafeDistanceSquared()) {
                this.strafingBackwards = true;
            }

            this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
            this.entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
        } else {
            this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
        }

        if (this.isActive()) {
            if (!canSee && this.seeTime < -60) {
                this.deactivate();
            } else if (canSee) {
                float indirect = isIndirect(distanceSq, entitylivingbase) ? 1F : 0F;
                this.deactivate();
                this.entity.attackEntityWithRangedAttack(entitylivingbase, indirect);
                // ChloePrime Start:
                // Redirect hardcoded attack CD to the attack CD from NPC's ranged stats.
                INPCRanged rangedData = this.entity.stats.ranged;
                this.attackTime = rangedData.getDelayRNG();
            }
        } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
            this.activate();
        }
    }

    private void deactivate() {
        active = false;
    }

    private void activate() {
        active = true;
    }

    private boolean isActive() {
        return active;
    }

    private boolean isIndirect(double distanceSq, EntityLivingBase target) {
        switch (this.entity.stats.ranged.getFireType()) {
            case 1:
                return distanceSq > getMaxAttackDistance() / 2.0D;
            case 2:
                return !this.entity.getEntitySenses().canSee(target);
            default:
                return false;
        }
    }

    public static class Builder {
        public NpcAiSkeletonRangedAttack create(EntityNPCInterface npc) {
            return new NpcAiSkeletonRangedAttack(npc, moveSpeedAmp, farSq, nearSq, tanProb, normProb);
        }

        public Builder boostSpeed(double moveSpeedAmp) {
            this.moveSpeedAmp = moveSpeedAmp;
            return this;
        }

        public Builder maxStrafeRangeScale(double farSq) {
            this.farSq = farSq;
            return this;
        }

        public Builder minStrafeRangeScale(double nearSq) {
            this.nearSq = nearSq;
            return this;
        }

        public Builder tangentSwapProbabilityWhenStrafing(float probably) {
            tanProb = probably;
            return this;
        }

        public Builder normalSwapProbabilityWhenStrafing(float probably) {
            normProb = probably;
            return this;
        }

        private double moveSpeedAmp = 1.0;
        private double farSq = VANILLA_FAR;
        private double nearSq = VANILLA_NEAR;
        private float tanProb = VANILLA_TANGENT_SWAP_RATE;
        private float normProb = VANILLA_NORMAL_SWAP_RATE;
    }
}
