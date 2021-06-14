package moe.gensoukyo.lib.rpg;

import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * 伤害信息可自定义的EntityDamageSource
 *
 * @author ChloePrime
 */
class MsgCustomizedEntityDamageSource extends EntityDamageSource {
    @Nullable
    private ITextComponent deathMessage = null;
    private Supplier<ITextComponent> lazyDeathMessage;

    public MsgCustomizedEntityDamageSource(
            @Nullable Entity damageSourceEntityIn,
            @Nonnull Supplier<ITextComponent> deathMsgIn
    ) {
        super("bun", damageSourceEntityIn);

        Preconditions.checkNotNull(deathMsgIn);
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
