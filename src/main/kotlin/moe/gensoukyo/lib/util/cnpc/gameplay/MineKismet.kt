@file:Suppress("unused")

package moe.gensoukyo.lib.util.cnpc.gameplay

import moe.gensoukyo.lib.util.cnpc.npcApi
import moe.gensoukyo.lib.util.fastNormalize
import moe.gensoukyo.lib.util.fastSqrt
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.util.EntitySelectors
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import noppes.npcs.api.IWorld
import noppes.npcs.api.entity.IEntity
import noppes.npcs.api.wrapper.WorldWrapper
import noppes.npcs.controllers.PixelmonHelper
import noppes.npcs.entity.EntityNPCInterface
import noppes.npcs.entity.EntityProjectile
import java.lang.invoke.MethodHandles
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Predicate
import java.util.stream.Stream
import javax.vecmath.Vector3d
import javax.vecmath.Vector3f

/**
 * Get entities inside a box space.
 *
 * @param x X coordinate
 * @param y Y coordinate
 * @param z Z coordinate
 * @param type Which type of entity to fetch
 * @param predicate Entity filter passed to Mojang. Can produce less garbage than [Stream.filter]
 */
@JvmOverloads
@Suppress("UNCHECKED_CAST")
fun <T : Entity> IWorld.cubeCast(
    x: Double, y: Double, z: Double, range: Double,
    type: Class<out T>,
    predicate: Predicate<in IEntity<T>> = defaultPredicate()
): Stream<IEntity<T>> {
    val obesity = range + 0.5
    val bb = AxisAlignedBB(
        x - obesity, y - obesity, z - obesity,
        x + obesity, y + obesity, z + obesity
    )
    val entities = mcWorld.getEntitiesWithinAABB(type, bb) {
        predicate.test(it!!.npcApi as IEntity<T>)
    }
    return entities.stream().map { it.npcApi as IEntity<T> }
}

/**
 * Get entities inside a sphere space.
 *
 * @param x X coordinate
 * @param y Y coordinate
 * @param z Z coordinate
 * @param type Which type of entity to fetch
 * @param predicate Entity filter passed to Mojang. Can produce less garbage than [Stream.filter]
 */
@JvmOverloads
@Suppress("UNCHECKED_CAST")
fun <T : Entity> IWorld.sphereCast(
    x: Double, y: Double, z: Double, range: Double,
    type: Class<out T>,
    predicate: Predicate<in IEntity<T>> = defaultPredicate()
): Stream<IEntity<T>> {
    val rangeSq = range * range
    return cubeCast(x, y, z, range, type, predicate).filter {
        val dx = it.x - x
        val dy = it.y - y
        val dz = it.z - z
        dx * dx + dy * dy + dz * dz <= rangeSq
    }
}

/**
 * Reified version of [cubeCast]
 *
 * @param T Which type of entity to fetch
 * @param x X coordinate
 * @param y Y coordinate
 * @param z Z coordinate
 */
inline fun <reified T : Entity> IWorld.cubeCast(
    x: Double, y: Double, z: Double, range: Double
) = cubeCast(x, y, z, range, T::class.java)


/**
 * Reified version of [sphereCast]
 *
 * @param T Which type of entity to fetch
 * @param x X coordinate
 * @param y Y coordinate
 * @param z Z coordinate
 */
inline fun <reified T : Entity> IWorld.sphereCast(
    x: Double, y: Double, z: Double, range: Double
) = sphereCast(x, y, z, range, T::class.java)

/**
 * @param type see [noppes.npcs.api.constants.EntityType]
 */
fun IWorld.cubeCast(
    x: Double, y: Double, z: Double, range: Double, type: Int
) = cubeCast(x, y, z, range, getClassForType(type))

/**
 * @param type see [noppes.npcs.api.constants.EntityType]
 */
fun IWorld.sphereCast(
    x: Double, y: Double, z: Double, range: Double, type: Int
) = sphereCast(x, y, z, range, getClassForType(type))

@JvmOverloads
fun randomDirection(rng: Random = DEFAULT_RNG): Vector3d {
    val x = rng.nextGaussian()
    val y = rng.nextGaussian()
    val z = rng.nextGaussian()

    return Vector3d(x, y, z).also {
        it.fastNormalize()
    }
}



internal fun <T : Entity> defaultPredicate() = DEFAULT_PREDICATE as Predicate<in IEntity<T>>

private val DEFAULT_PREDICATE: Predicate<in IEntity<*>> = Predicate {
    EntitySelectors.NOT_SPECTATING.test(it.mcEntity)
}

private val entityTypes = arrayOf(
    // 0
    Entity::class.java,
    // 1
    EntityPlayer::class.java,
    // 2
    EntityNPCInterface::class.java,
    // 3
    EntityMob::class.java,
    // 4
    EntityAnimal::class.java,
    // 5
    EntityLivingBase::class.java,
    // 6
    EntityItem::class.java,
    // 7
    EntityProjectile::class.java,
    // 8
    PixelmonHelper.getPixelmonClass(),
    // 9
    EntityVillager::class.java,
    // 10
    EntityArrow::class.java,
    // 11
    EntityThrowable::class.java
)

@Suppress("UNCHECKED_CAST")
private fun IWorld.getClassForType(type: Int): Class<out Entity> {
    val rawType = if (mhGetClassForType != null) {
        mhGetClassForType.invoke(this, type)
    } else {
        entityTypes[MathHelper.clamp(type, 0, Int.MAX_VALUE)]
    }
    return rawType as Class<out Entity>
}

private val mhGetClassForType = try {
     MethodHandles.lookup().unreflect(
        WorldWrapper::class.java.getDeclaredMethod("getClassForType").apply {
            isAccessible = true
        }
    )
} catch (ex: NoSuchMethodException) {
    null
}

private val DEFAULT_RNG = Random()