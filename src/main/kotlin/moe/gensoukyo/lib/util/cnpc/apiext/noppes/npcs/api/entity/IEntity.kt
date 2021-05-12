@file:Suppress("Unused")
package moe.gensoukyo.lib.util.cnpc.apiext.noppes.npcs.api.entity

import moe.gensoukyo.lib.constants.ModIds
import net.minecraft.entity.Entity
import net.minecraftforge.fml.common.Optional
import noppes.npcs.api.entity.IEntity
import java.util.*
import javax.vecmath.Vector3d

/**
 * uuid对象
 */
val <T: Entity> IEntity<T>.uuidObject: UUID
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.mcEntity.uniqueID

/**
 * 是否不发出声音
 */
var <T: Entity> IEntity<T>.isSilent: Boolean
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.mcEntity.isSilent
    @Optional.Method(modid = ModIds.CNPC)
    set(value) {
        this.mcEntity.isSilent = value
    }

/**
 * 是否无重力
 */
var <T: Entity> IEntity<T>.isGravityIgnored: Boolean
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.mcEntity.hasNoGravity()
    @Optional.Method(modid = ModIds.CNPC)
    set(value) = this.mcEntity.setNoGravity(value)

/**
 * 视线方向向量
 */
val <T: Entity> IEntity<T>.directionOfSight: Vector3d
    @Optional.Method(modid = ModIds.CNPC)
    get() {
        val mcpVector = this.mcEntity.lookVec
        return Vector3d(mcpVector.x, mcpVector.y, mcpVector.z)
    }

/**
 * 是否发光
 */
var <T: Entity> IEntity<T>.isGlowing: Boolean
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.mcEntity.isGlowing
    @Optional.Method(modid = ModIds.CNPC)
    set(value) {
        this.mcEntity.isGlowing = value
    }

/**
 * 隐形
 */
var <T: Entity> IEntity<T>.isVisible: Boolean
    @Optional.Method(modid = ModIds.CNPC)
    get() = !this.mcEntity.isInvisible
    @Optional.Method(modid = ModIds.CNPC)
    set(value) {
        this.mcEntity.isInvisible = !value
    }

/**
 * 氧气点数
 */
var <T: Entity> IEntity<T>.airPoints: Int
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.mcEntity.air
    @Optional.Method(modid = ModIds.CNPC)
    set(value) {
        this.mcEntity.air = value
    }

/**
 * 原版无敌
 * 不要把NPC的这个属性设置为true
 */
var <T: Entity> IEntity<T>.isInvulnerable: Boolean
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.mcEntity.isInvulnerable
    @Optional.Method(modid = ModIds.CNPC)
    set(value) = this.mcEntity.setEntityInvulnerable(value)

/**
 * 获取/切换维度
 */
var <T: Entity> IEntity<T>.dimension: Int
    @Optional.Method(modid = ModIds.CNPC)
    get() = this.mcEntity.dimension
    @Optional.Method(modid = ModIds.CNPC)
    set(value) {
        if (this.mcEntity.dimension == value) return
        this.mcEntity.changeDimension(value)
    }
