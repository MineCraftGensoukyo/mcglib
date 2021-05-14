/**
 * 允许自定义死亡信息的攻击行为，
 * 以及包式真伤。
 *
 * @author ChloePrime
 */
@file:Suppress("unused")

package moe.gensoukyo.lib.rpg

import moe.gensoukyo.lib.constants.ModIds
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.fml.common.Optional
import noppes.npcs.api.entity.IEntityLivingBase

@Optional.Method(modid = ModIds.CNPC)
fun <T: EntityLivingBase> IEntityLivingBase<T>.damage(amount: Float, deathMsg: String)
    = DamageUtils.attackEntityWithCustomDeathMessage(this, amount, deathMsg)

@Optional.Method(modid = ModIds.CNPC)
fun <T: EntityLivingBase> IEntityLivingBase<T>.damage(
    damager: IEntityLivingBase<*>,
    amount: Float,
    deathMsg: String
) {
    DamageUtils.attackEntityWithCustomDeathMessage(this, damager, amount, deathMsg)
}

@Optional.Method(modid = ModIds.CNPC)
fun <T: EntityLivingBase> IEntityLivingBase<T>.bunStyleTrueDamage(amount: Float) {
    DamageUtils.doBunStyleTrueDamage(this, amount)
}

@Optional.Method(modid = ModIds.CNPC)
fun <T: EntityLivingBase> IEntityLivingBase<T>.bunStyleTrueDamage(amount: Float, deathMsg: String) {
    DamageUtils.doBunStyleTrueDamage(this, amount, deathMsg)
}