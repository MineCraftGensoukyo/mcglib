@file:Suppress("unused")

package moe.gensoukyo.lib.util.cnpc.gameplay

import moe.gensoukyo.lib.util.EntityAudio
import net.minecraft.util.SoundCategory
import noppes.npcs.api.entity.IEntity

/**
 * Play sound to players nearby.
 */
@JvmOverloads
fun IEntity<*>.playSound(
    sound: String,
    volume: Float = 1F,
    pitch: Float = 1F
) {
    this.world.playSoundAt(this.pos, sound, volume, pitch)
}

/**
 * Binds a sound to an entity.
 * Commonly used to implement the flying sound of rockets.
 *
 * @param eventRange Players outside this range will NOT hear the sound even if they came near to [this] npc later.
 */
@JvmOverloads
fun IEntity<*>.bindSound(
    sound: String,
    categoryIn: Int = SoundCategory.MASTER.ordinal,
    volume: Float = 1F,
    pitch: Float = 1F,
    eventRange: Double = 256.0
) {
    val category = SoundCategory.values()[categoryIn]
    EntityAudio.bindSound(mcEntity, sound, category, volume, pitch, eventRange)
}

/**
 * Binds a sound to an entity,
 * Commonly used to implement the flying sound of rockets.
 *
 * @param eventRange Players outside this range will NOT hear the sound even if they came near to [this] npc later.
 */
@JvmOverloads
fun IEntity<*>.bindSound(
    sound: String,
    categoryIn: String,
    volumeIn: Float = 1F,
    pitchIn: Float = 1F,
    eventRange: Double = 256.0
) {
    val category = SoundCategory.getByName(categoryIn.toLowerCase())
    EntityAudio.bindSound(mcEntity, sound, category, volumeIn, pitchIn, eventRange)
}