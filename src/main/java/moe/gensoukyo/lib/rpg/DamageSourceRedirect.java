package moe.gensoukyo.lib.rpg;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import noppes.npcs.api.IDamageSource;

import javax.annotation.Nullable;

/**
 * Used to redirect the damage from on entity to another to block damages directly called on entity.
 * Catch a DamageSource from event, generate a redirected damageSource, call damage method on another entity.
 * In another entity, filter damageSource by instanceof or REDIRECT prefix.
 */
public class DamageSourceRedirect extends DamageSource {

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

    public DamageSourceRedirect(IDamageSource source) {
        this(source.getMCDamageSource());
    }

    @Override
    public boolean isUnblockable() {
        return source.isUnblockable();
    }

    @Override
    public boolean isProjectile() {
        return source.isProjectile();
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
