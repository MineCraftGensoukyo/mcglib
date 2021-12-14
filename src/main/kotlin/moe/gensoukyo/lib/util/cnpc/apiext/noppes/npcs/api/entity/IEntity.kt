@file:Suppress("Unused")

package moe.gensoukyo.lib.util.cnpc.apiext.noppes.npcs.api.entity

import moe.gensoukyo.lib.util.cnpc.npcApi
import net.minecraft.entity.Entity
import noppes.npcs.api.INbt
import noppes.npcs.api.entity.IEntity
import java.util.*
import javax.vecmath.Vector3d

/**
 * 无敌时间
 */
var <T : Entity> IEntity<T>.invulnerableTime: Int
    get() = this.mcEntity.hurtResistantTime
    set(value) {
        this.mcEntity.hurtResistantTime = value
    }

/**
 * uuid对象
 */
val <T : Entity> IEntity<T>.uuidObject: UUID
    get() = this.mcEntity.uniqueID

/**
 * 是否不发出声音
 */
var <T : Entity> IEntity<T>.isSilent: Boolean
    get() = this.mcEntity.isSilent
    set(value) {
        this.mcEntity.isSilent = value
    }

/**
 * 是否无重力
 */
var <T : Entity> IEntity<T>.isGravityIgnored: Boolean
    get() = this.mcEntity.hasNoGravity()
    set(value) = this.mcEntity.setNoGravity(value)

/**
 * 视线方向向量
 */
val <T : Entity> IEntity<T>.directionOfSight: Vector3d
    get() {
        val mcpVector = this.mcEntity.lookVec
        return Vector3d(mcpVector.x, mcpVector.y, mcpVector.z)
    }

/**
 * 是否发光
 */
var <T : Entity> IEntity<T>.isGlowing: Boolean
    get() = this.mcEntity.isGlowing
    set(value) {
        this.mcEntity.isGlowing = value
    }

/**
 * 隐形
 */
var <T : Entity> IEntity<T>.isVisible: Boolean
    get() = !this.mcEntity.isInvisible
    set(value) {
        this.mcEntity.isInvisible = !value
    }

/**
 * 氧气点数
 */
var <T : Entity> IEntity<T>.airPoints: Int
    get() = this.mcEntity.air
    set(value) {
        this.mcEntity.air = value
    }

/**
 * 原版无敌
 * 不要把NPC的这个属性设置为true
 */
var <T : Entity> IEntity<T>.isInvulnerable: Boolean
    get() = this.mcEntity.isInvulnerable
    set(value) = this.mcEntity.setEntityInvulnerable(value)

/**
 * 获取/切换维度
 */
var <T : Entity> IEntity<T>.dimension: Int
    get() = this.mcEntity.dimension
    set(value) {
        if (this.mcEntity.dimension == value) return
        this.mcEntity.changeDimension(value)
    }

/**
 * 是否在地面上
 */
var <T : Entity> IEntity<T>.isOnGround: Boolean
    get() = this.mcEntity.onGround
    set(value) {
        this.mcEntity.onGround = value
    }

/**
 * 跌落距离
 */
var <T : Entity> IEntity<T>.fallDistance: Float
    get() = this.mcEntity.fallDistance
    set(value) {
        this.mcEntity.fallDistance = value
    }

/**
 * 上台阶的最大台阶高度
 */
var <T : Entity> IEntity<T>.stepHeight: Float
    get() = this.mcEntity.stepHeight
    set(value) {
        this.mcEntity.stepHeight = value
    }

/**
 * noclip
 */
var <T : Entity> IEntity<T>.hasNoClip: Boolean
    get() = this.mcEntity.noClip
    set(value) {
        this.mcEntity.noClip = value
    }

/**
 * 存活时间
 */
var <T : Entity> IEntity<T>.ticksExisted: Int
    get() = this.mcEntity.ticksExisted
    set(value) {
        this.mcEntity.ticksExisted = value
    }

/**
 * 是否禁止实体更新
 */
var <T : Entity> IEntity<T>.frozen: Boolean
    get() = this.mcEntity.updateBlocked
    set(value) {
        this.mcEntity.updateBlocked = value
    }

fun <T : Entity> IEntity<T>.serialize() = mcEntity.serializeNBT().npcApi
fun <T : Entity> IEntity<T>.deserialize(nbt: INbt) = mcEntity.deserializeNBT(nbt.mcnbt)