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

val Entity.bukkit get(): BukkitEntity
= mc2Bukkit.invoke(this) as BukkitEntity

val BukkitEntity.handle get(): Entity {
    initBukkit2McHandle(this)
    return (bukkit2MC?.invoke(this) as? Entity)
        ?: throw UnsupportedOperationException()
}

// Derived Entity Types

val EntityLivingBase.bukkit get(): BukkitLivingEntity
= (this as Entity).bukkit as BukkitLivingEntity

val BukkitLivingEntity.handle get()
= (this as BukkitEntity).handle as EntityLivingBase

val EntityPlayer.bukkit get(): BukkitPlayer
= (this as Entity).bukkit as BukkitPlayer

val BukkitPlayer.handle get(): EntityPlayer
= (this as BukkitEntity).handle as EntityPlayer