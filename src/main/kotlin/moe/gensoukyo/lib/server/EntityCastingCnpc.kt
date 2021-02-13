@file:Suppress("unused")
@file:SideOnly(Side.SERVER)

package moe.gensoukyo.lib.server

import moe.gensoukyo.lib.constants.ModIds
import moe.gensoukyo.lib.util.cnpc.npcApi
import net.minecraft.entity.Entity as Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import noppes.npcs.api.entity.IEntity
import noppes.npcs.api.entity.IEntityLivingBase
import noppes.npcs.api.entity.IPlayer
import org.bukkit.entity.Entity as BukkitEntity
import org.bukkit.entity.LivingEntity as BukkitLivingEntity
import org.bukkit.entity.Player as BukkitPlayer

// Basic Entity Types

val <T: Entity> IEntity<T>.bukkit: BukkitEntity
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = this.mcEntity.bukkit

val BukkitEntity.npcApi: IEntity<*>
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = this.handle.npcApi

// Derived Entity Types

val <T: EntityLivingBase> IEntityLivingBase<T>.bukkit: BukkitLivingEntity
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = this.mcEntity.bukkit

val BukkitLivingEntity.npcApi: IEntityLivingBase<*>
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = (this as BukkitEntity).npcApi as IEntityLivingBase<*>

val <T: EntityPlayerMP> IPlayer<T>.bukkit: BukkitPlayer
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = this.mcEntity.bukkit

val BukkitPlayer.npcApi: IPlayer<*>
    @Optional.Method(modid = ModIds.CNPC)
    inline get() = (this as BukkitEntity).npcApi as IPlayer<*>