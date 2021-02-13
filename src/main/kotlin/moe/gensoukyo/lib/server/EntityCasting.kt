@file:Suppress("unused")
@file:SideOnly(Side.SERVER)

package moe.gensoukyo.lib.server

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.Entity as Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.bukkit.entity.Entity as BukkitEntity
import org.bukkit.entity.LivingEntity as BukkitLivingEntity
import org.bukkit.entity.Player as BukkitPlayer

// Basic Entity Types

val Entity.bukkit: BukkitEntity
    get() = mc2Bukkit.invoke(this) as BukkitEntity

val BukkitEntity.handle: Entity
    get() {
        initBukkit2McHandle(this)
        return (bukkit2MC?.invoke(this) as? Entity)
            ?: throw UnsupportedOperationException()
    }

// Derived Entity Types

val EntityLivingBase.bukkit: BukkitLivingEntity
    inline get() = (this as Entity).bukkit as BukkitLivingEntity

val BukkitLivingEntity.handle: EntityLivingBase
    inline get() = (this as BukkitEntity).handle as EntityLivingBase

val EntityPlayer.bukkit: BukkitPlayer
    inline get() = (this as Entity).bukkit as BukkitPlayer

val BukkitPlayer.handle: EntityPlayer
    inline get() = (this as BukkitEntity).handle as EntityPlayer