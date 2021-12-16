package moe.gensoukyo.lib.internal.core.mixins;

import moe.gensoukyo.lib.util.cnpc.ai.CnpcAiAccessor;
import moe.gensoukyo.lib.util.cnpc.ai.CnpcAiBuilder;
import moe.gensoukyo.lib.util.cnpc.ai.CustomNpcMeleeAiWrapper;
import moe.gensoukyo.lib.util.cnpc.ai.CustomNpcRangedAiWrapper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import noppes.npcs.ai.EntityAIAttackTarget;
import noppes.npcs.ai.EntityAIRangedAttack;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author ChloePrime
 * @see CnpcAiBuilder
 */
@Mixin(
        value = EntityNPCInterface.class,
        priority = 500,
        remap = false
)
public abstract class MixinNpcInterface
        extends EntityCreature
        implements CnpcAiAccessor, CnpcAiBuilder {

    @Shadow private int taskCount;
    @Shadow public boolean updateAI;

    private @Nullable Consumer<CnpcAiBuilder> all2;
    private @Nullable Consumer<CnpcAiBuilder> regular;
    private @Nullable Consumer<CnpcAiBuilder> door;
    private @Nullable Consumer<CnpcAiBuilder> shelter;
    private @Nullable Consumer<CnpcAiBuilder> battle;
    private @Nullable Consumer<CnpcAiBuilder> move;
    private @Nullable Supplier<EntityAIAttackTarget> meleeReplacement;
    private @Nullable Supplier<EntityAIRangedAttack> rangedReplacement;

    @Inject(
            at = @At("HEAD"),
            method = "updateTasks",
            cancellable = true
    )
    private void injectTasks(CallbackInfo ci) {
        if (isContextInvalid()) {
            return;
        }
        inject0(all2, ci);
    }

    @Inject(
            at = @At("HEAD"),
            method = "addRegularEntries",
            cancellable = true
    )
    private void injectRegularAi(CallbackInfo ci) {
        inject0(regular, ci);
    }

    @Inject(
            at = @At("HEAD"),
            method = "doorInteractType",
            cancellable = true
    )
    private void injectDoorInteractAi(CallbackInfo ci) {
        inject0(door, ci);
    }

    @Inject(
            at = @At("HEAD"),
            method = "seekShelter",
            cancellable = true
    )
    private void injectSeekShelterAi(CallbackInfo ci) {
        inject0(shelter, ci);
    }

    @Inject(
            at = @At("HEAD"),
            method = "setResponse",
            cancellable = true
    )
    private void injectBattleAi(CallbackInfo ci) {
        inject0(battle, ci);
    }

    @Inject(
            at = @At("HEAD"),
            method = "setMoveType",
            cancellable = true
    )
    private void injectMoveAi(CallbackInfo ci) {
        inject0(move, ci);
    }

    private void inject0(@Nullable Consumer<CnpcAiBuilder> consumer, CallbackInfo ci) {
        if (consumer != null) {
            consumer.accept(this);
            ci.cancel();
        }
    }

    /**
     * 替换近战 AI
     */
    @Redirect(
            method = "setResponse",
            at = @At(value = "NEW", target = "noppes.npcs.ai.EntityAIAttackTarget", remap = false)
    )
    private EntityAIAttackTarget redirectMeleeAiConstructor(EntityNPCInterface par1) {
        return meleeReplacement != null
                ? meleeReplacement.get()
                : new EntityAIAttackTarget(thisNpc());
    }

    /**
     * 替换远程 AI
     */
    @Redirect(
            method = "setResponse",
            at = @At(value = "NEW", target = "noppes.npcs.ai.EntityAIRangedAttack", remap = false)
    )
    private EntityAIRangedAttack redirectRangedAiConstructor(IRangedAttackMob par1) {
        return rangedReplacement != null
                ? rangedReplacement.get()
                : new EntityAIRangedAttack(thisNpc());
    }

    /**
     * Is world null? or are we in client environment?
     */
    private boolean isContextInvalid() {
        return world == null || world.isRemote;
    }

    @Inject(
            method = "reset",
            at = @At(
                    value = "INVOKE",
                    target = "Lnoppes/npcs/EventHooks;onNPCInit(Lnoppes/npcs/entity/EntityNPCInterface;)V"
            )
    )
    private void injectReset(CallbackInfo ci) {
        resetOverrides();
    }

    // Implementations

    @Override
    public void overrideAllAiCompletely(@Nullable Consumer<CnpcAiBuilder> builder) {
        all2 = builder;
        updateAI = true;
    }

    @Override
    public void overrideRegularAi(@Nullable Consumer<CnpcAiBuilder> builder) {
        regular = builder;
        updateAI = true;
    }

    @Override
    public void overrideDoorInteractionAi(@Nullable Consumer<CnpcAiBuilder> builder) {
        door = builder;
        updateAI = true;
    }

    @Override
    public void overrideSeekShelterAi(@Nullable Consumer<CnpcAiBuilder> builder) {
        shelter = builder;
        updateAI = true;
    }

    @Override
    public void overrideBattleAi(@Nullable Consumer<CnpcAiBuilder> builder) {
        battle = builder;
        updateAI = true;
    }

    @Override
    public void overrideMoveAi(@Nullable Consumer<CnpcAiBuilder> builder) {
        move = builder;
        updateAI = true;
    }

    @Override
    public void replaceMeleeAiTask(@Nullable Supplier<EntityAIBase> taskConstructor) {
        meleeReplacement = taskConstructor == null
                ? null
                : () -> castMeleeAi(taskConstructor.get());
    }

    @Override
    public void replaceRangedAiTask(@Nullable Supplier<EntityAIBase> taskConstructor) {
        rangedReplacement = taskConstructor == null
                ? null
                : () -> castRangedAi(taskConstructor.get());
    }

    private EntityAIAttackTarget castMeleeAi(EntityAIBase ai) {
        if (ai instanceof EntityAIAttackTarget) {
            return (EntityAIAttackTarget) ai;
        }
        return new CustomNpcMeleeAiWrapper(ai, thisNpc());
    }

    private EntityAIRangedAttack castRangedAi(EntityAIBase ai) {
        if (ai instanceof EntityAIRangedAttack) {
            return (EntityAIRangedAttack) ai;
        }
        return new CustomNpcRangedAiWrapper(ai, thisNpc());
    }

    private EntityNPCInterface thisNpc() {
        return (EntityNPCInterface) (Object) this;
    }

    @Override
    public int getTaskCounter() {
        return taskCount++;
    }

    @Override
    public int addTask(int relativePriority, @Nonnull EntityAIBase task) {
        taskCount += relativePriority;

        int tc = taskCount - 1;
        tasks.addTask(tc, task);
        return tc;
    }

    public MixinNpcInterface(World worldIn) {
        super(worldIn);
    }
}