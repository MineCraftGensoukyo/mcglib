package moe.gensoukyo.lib.rpg;

import moe.gensoukyo.lib.constants.ModIds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Optional;
import noppes.npcs.api.entity.IEntityLivingBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author ChloePrime
 */
@SuppressWarnings("unused")
public class DamageUtils {
    private DamageUtils() { }

    /**
     * 造成伤害并自定义死亡消息
     * @param victim   受到伤害的实体，被攻击者
     * @param amount   伤害量
     * @param deathMsg 自定义的死亡信息
     */
    public static void attackEntityWithCustomDeathMessage(
            @Nonnull EntityLivingBase victim,
            float amount,
            String deathMsg) {
        attackEntityWithCustomDeathMessage0(victim, null, amount, deathMsg);
    }

    /**
     * 造成伤害并自定义死亡消息
     * @param victim   受到伤害的实体，被攻击者
     * @param damager  造成伤害的实体，攻击者
     * @param amount   伤害量
     * @param deathMsg 自定义的死亡信息
     */
    public static void attackEntityWithCustomDeathMessage(
            @Nonnull EntityLivingBase victim,
            @Nonnull EntityLivingBase damager,
            float amount,
            @Nonnull String deathMsg
    ) {
        attackEntityWithCustomDeathMessage0(victim, damager, amount, deathMsg);
    }

    private static void attackEntityWithCustomDeathMessage0(
            @Nonnull EntityLivingBase victim,
            @Nullable EntityLivingBase damager,
            float amount,
            @Nonnull String deathMsg
    ) {
        DamageSource damageSource = causeMsgCustomizedDamage(damager, deathMsg);
        victim.attackEntityFrom(damageSource, amount);
    }

    public static DamageSource causeMsgCustomizedDamage(@Nonnull String deathMsg) {
        return causeMsgCustomizedDamage(null, deathMsg);
    }

    public static DamageSource causeMsgCustomizedDamage(
            @Nullable EntityLivingBase damager,
            @Nonnull String deathMsg
    ) {
        return (damager != null)
                ? new MsgCustomizedEntityDamageSource(damager, deathMsg)
                : new MsgCustomizedDamageSource(deathMsg);
    }


    /**
     * 制造包式真伤
     * 在致死前使用setHealth来扣除生命，
     * 致死时使用穿透伤害
     *
     * @param victim 受害者
     * @param amount 伤害量
     */
    public static void doBunStyleTrueDamage(EntityLivingBase victim, float amount) {
        if (victim.world.isRemote) {
            return;
        }
        doBunStyleTrueDamage(victim, amount, victim.getDisplayName() + "被神奇的魔法杀死了");
    }

    /**
     * 制造包式真伤
     * 在致死前使用setHealth来扣除生命，
     * 致死时使用穿透伤害
     *
     * @param victim   受害者
     * @param amount   伤害量
     * @param deathMsg 自定义死亡消息
     */
    public static void doBunStyleTrueDamage(EntityLivingBase victim, float amount, String deathMsg) {
        if (victim.world.isRemote) {
            return;
        }
        if (victim instanceof EntityPlayerMP) {
            boolean isPlayerMode = ((EntityPlayerMP) victim).interactionManager.getGameType().isSurvivalOrAdventure();
            if (!isPlayerMode) {
                return;
            }
        }
        float curHealth = victim.getHealth();
        if (curHealth > amount) {
            victim.setHealth(curHealth - amount);
        } else {
            victim.attackEntityFrom(causeFinalHitDamage(deathMsg), Float.MAX_VALUE);
        }
    }

    private static DamageSource causeFinalHitDamage(String msg) {
        return new MsgCustomizedDamageSource(msg).setDamageBypassesArmor();
    }

    // CNPC支持

    @Optional.Method(modid = ModIds.CNPC)
    public static void attackEntityWithCustomDeathMessage(
            @Nonnull IEntityLivingBase<?> victim,
            float amount,
            String deathMsg) {
        attackEntityWithCustomDeathMessage0(victim.getMCEntity(), null, amount, deathMsg);
    }

    @Optional.Method(modid = ModIds.CNPC)
    public static void attackEntityWithCustomDeathMessage(
            @Nonnull IEntityLivingBase<?> victim,
            @Nonnull IEntityLivingBase<?> damager,
            float amount,
            @Nonnull String deathMsg
    ) {
        attackEntityWithCustomDeathMessage0(
                victim.getMCEntity(), damager.getMCEntity(),
                amount, deathMsg
        );
    }

    @Optional.Method(modid = ModIds.CNPC)
    public static void doBunStyleTrueDamage(IEntityLivingBase<?> victim, float amount) {
        doBunStyleTrueDamage(victim.getMCEntity(), amount);
    }

    @Optional.Method(modid = ModIds.CNPC)
    public static void doBunStyleTrueDamage(IEntityLivingBase<?> victim, float amount, String deathMsg) {
        doBunStyleTrueDamage(victim.getMCEntity(), amount, deathMsg);
    }
}
