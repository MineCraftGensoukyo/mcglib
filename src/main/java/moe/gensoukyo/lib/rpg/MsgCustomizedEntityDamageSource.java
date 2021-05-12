package moe.gensoukyo.lib.rpg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 伤害信息可自定义的EntityDamageSource
 * @author ChloePrime
 */
public class MsgCustomizedEntityDamageSource extends EntityDamageSource {
    private final ITextComponent deathMessage;

    public MsgCustomizedEntityDamageSource(@Nullable Entity damageSourceEntityIn, String deathMsgIn) {
        super("bun", damageSourceEntityIn);
        deathMessage = new TextComponentTranslation(deathMsgIn);
    }

    /**
     * 其实这个方法只会在服务端执行
     */
    @Nonnull
    @Override
    public ITextComponent getDeathMessage(@Nonnull EntityLivingBase entityLivingBaseIn) {
        return deathMessage;
    }
}
