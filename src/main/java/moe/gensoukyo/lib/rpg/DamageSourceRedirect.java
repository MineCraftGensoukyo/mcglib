package moe.gensoukyo.lib.rpg;

import moe.gensoukyo.lib.constants.ModIds;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.Optional;
import noppes.npcs.api.IDamageSource;

import javax.annotation.Nullable;

/**
 * Used to redirect the damage from on entity to another to block damages directly called on entity.
 * Catch a DamageSource from event, generate a redirected damageSource, call damage method on another entity.
 * In another entity, filter damageSource by instanceof or "redirect" prefix.
 */
public class DamageSourceRedirect extends DamageSource {

    @Optional.Method(modid = ModIds.CNPC)
    public static DamageSourceRedirect wrap(IDamageSource source) {
        return new DamageSourceRedirect(source.getMCDamageSource());
    }

    public static DamageSourceRedirect wrap(DamageSource source) {
        return new DamageSourceRedirect(source);
    }

    private final DamageSource source;

    public DamageSourceRedirect(DamageSource source) {
        super("redirect");
        DamageSource begin = source;
        while (source instanceof DamageSourceRedirect) {
            source = ((DamageSourceRedirect) source).source;
            if (source == begin) throw new IllegalStateException("self-linked redirect chain");
        }
        this.source = source;
    }

    @Override
    public boolean isUnblockable() {
        return source.isUnblockable();
    }

    @Override
    public boolean isProjectile() {
        return source.isProjectile();
    }

    @Override
    public boolean isCreativePlayer() {
        return source.isCreativePlayer();
    }

    @Override
    public boolean isDamageAbsolute() {
        return source.isDamageAbsolute();
    }

    @Override
    public boolean isDifficultyScaled() {
        return source.isDifficultyScaled();
    }

    @Override
    public boolean isExplosion() {
        return source.isExplosion();
    }

    @Override
    public boolean isFireDamage() {
        return source.isFireDamage();
    }

    @Override
    public boolean isMagicDamage() {
        return source.isMagicDamage();
    }

    @Nullable
    @Override
    public Entity getTrueSource() {
        return source.getTrueSource();
    }

    @Nullable
    @Override
    public Entity getImmediateSource() {
        return source.getImmediateSource();
    }

    public DamageSource getOriginalSource() {
        return source;
    }
}
