package moe.gensoukyo.lib.rpg;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * 伤害信息可自定义的DamageSource
 *
 * @author ChloePrime
 */
class MsgCustomizedDamageSource extends DamageSource {
    @Nullable
    private ITextComponent deathMessage = null;
    private Supplier<ITextComponent> lazyDeathMessage;

    MsgCustomizedDamageSource(Supplier<ITextComponent> deathMsgIn) {
        super("bun");
        lazyDeathMessage = deathMsgIn;
    }

    /**
     * 其实这个方法只会在服务端执行
     */
    @Nonnull
    @Override
    public ITextComponent getDeathMessage(@Nonnull EntityLivingBase entityLivingBaseIn) {
        if (deathMessage == null) {
            deathMessage = lazyDeathMessage.get();
            lazyDeathMessage = null;
        }
        return deathMessage;
    }
}
