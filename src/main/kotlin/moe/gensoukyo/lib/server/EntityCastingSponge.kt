@file:Suppress("unused")

package moe.gensoukyo.lib.server

import moe.gensoukyo.lib.constants.ModIds
import moe.gensoukyo.lib.util.cnpc.npcApi
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.common.Optional
import noppes.npcs.api.entity.IEntity
import noppes.npcs.api.entity.IEntityLivingBase
import noppes.npcs.api.entity.IPlayer
import org.spongepowered.api.entity.Entity as SpongeEntity
import org.spongepowered.api.entity.living.Living as SpongeLivingEntity
import org.spongepowered.api.entity.living.player.Player as SpongePlayer

// Basic Entity Types

val Entity.sponge: SpongeEntity
    get() = this as SpongeEntity

val SpongeEntity.handle: Entity
    get() = this as Entity

// Derived Entity Types

val EntityLivingBase.sponge: SpongeLivingEntity
    get() = this as SpongeLivingEntity

val SpongeLivingEntity.handle: EntityLivingBase
    get() = this as EntityLivingBase

val EntityPlayer.sponge: SpongePlayer
    get() = this as SpongePlayer

val SpongePlayer.handle: EntityPlayer
    get() = this as EntityPlayer

// CNPC

val <T : Entity> IEntity<T>.sponge: SpongeEntity
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.mcEntity.sponge

val SpongeEntity.npcApi: IEntity<*>
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.handle.npcApi

val <T : EntityLivingBase> IEntityLivingBase<T>.sponge: SpongeLivingEntity
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.mcEntity.sponge

val SpongeLivingEntity.npcApi: IEntityLivingBase<*>
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.handle.npcApi

val <T : EntityPlayerMP> IPlayer<T>.sponge: SpongePlayer
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.mcEntity.sponge

val SpongePlayer.npcApi: IPlayer<*>
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.handle.npcApi