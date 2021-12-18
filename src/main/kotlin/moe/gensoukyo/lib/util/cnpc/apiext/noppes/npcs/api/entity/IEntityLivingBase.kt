@file:Suppress("Unused")

package moe.gensoukyo.lib.util.cnpc.apiext.noppes.npcs.api.entity

import moe.gensoukyo.lib.internal.common.util.distrust
import moe.gensoukyo.lib.rpg.DamageUtils
import moe.gensoukyo.lib.util.cnpc.npcApi
import net.minecraft.util.text.TextComponentString
import noppes.npcs.api.IDamageSource
import noppes.npcs.api.entity.IEntity
import noppes.npcs.api.item.IItemStack
import java.util.*
import java.util.function.Supplier
import javax.vecmath.Vector3d
import net.minecraft.entity.EntityLivingBase as McpType
import noppes.npcs.api.entity.IEntityLivingBase as ApiType

/**
 * 空中控制力
 */
var <T : McpType> ApiType<T>.airControlForce: Float
    get() = this.mcEntity.jumpMovementFactor
    set(value) {
        this.mcEntity.jumpMovementFactor = value
    }

/**
 * 随机数生成器
 */
val <T : McpType> ApiType<T>.rng: Random
    get() = this.mcEntity.rng

/**
 * 主动攻击计时器
 */
val <T : McpType> ApiType<T>.revengeTimer: Int
    get() = mcEntity.revengeTimer

/**
 * 仇恨对象
 */
var <T : McpType> ApiType<T>.lastMurderer: ApiType<*>?
    get() = distrust(mcEntity.lastAttackedEntity)?.npcApi
    set(value) {
        this.mcEntity.revengeTarget = value?.mcEntity
    }

/**
 * 仇恨计时器
 */
val <T : McpType> ApiType<T>.lastMurderTime: Int
    get() = mcEntity.lastAttackedEntityTime

/**
 * AI 闲置时间 ?
 */
val <T : McpType> ApiType<T>.idleTime: Int
    get() = mcEntity.idleTime

/**
 * 会触发治疗事件的治疗方式
 */
fun <T : McpType> ApiType<T>.addHealth(amount: Float) {
    mcEntity.heal(amount)
}

fun <T : McpType> ApiType<T>.renderItemBreak(item: IItemStack) {
    mcEntity.renderBrokenItemStack(item.mcItemStack)
}

fun <T : McpType> ApiType<T>.kill(damage: IDamageSource) {
    mcEntity.health = 0F
    mcEntity.onDeath(damage.mcDamageSource)
}

fun <T : McpType> ApiType<T>.kill(damager: ApiType<*>?, cause: Supplier<String>) {
    mcEntity.health = 0F
    mcEntity.onDeath(DamageUtils.causeMsgCustomizedDamage(damager?.mcEntity) {
        TextComponentString(cause.get())
    })
}

/**
 * 参数更多的击退
 */
fun <T : McpType> ApiType<T>.knockback(
    from: IEntity<*>, power: Float, dirX: Double, dirZ: Double
) {
    mcEntity.knockBack(from.mcEntity, power, dirX, dirZ)
}

/**
 * 是否正在爬梯子
 */
val <T : McpType> ApiType<T>.isClimbing: Boolean
    get() = mcEntity.isOnLadder


/**
 * 执行原版掉落伤害
 */
fun <T : McpType> ApiType<T>.doFallDamage(amount: Float) {
    mcEntity.fall(amount + 3F, 1F)
}

/**
 * 原版护甲值
 */
val <T : McpType> ApiType<T>.vanillaArmorValue: Int
    get() = mcEntity.totalArmorValue

fun <T : McpType> ApiType<T>.setJumping(value: Boolean) {
    mcEntity.setJumping(value)
}

val <T : McpType> ApiType<T>.lookDirection: Vector3d
    get() {
        val mcv = mcEntity.getLook(1F)
        return Vector3d(mcv.x, mcv.y, mcv.z)
    }

val <T : McpType> ApiType<T>.isElytraFlying: Boolean
    get() = mcEntity.isElytraFlying