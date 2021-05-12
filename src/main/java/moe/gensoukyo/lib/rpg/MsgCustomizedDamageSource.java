package moe.gensoukyo.lib.rpg;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;

/**
 * 伤害信息可自定义的DamageSource
 * @author ChloePrime
 */
public class MsgCustomizedDamageSource extends DamageSource {
    private final ITextComponent deathMessage;

    MsgCustomizedDamageSource(String deathMsgIn) {
        super("bun");
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
