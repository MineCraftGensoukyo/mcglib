package moe.gensoukyo.lib.internal.client.audio

import net.minecraft.client.audio.ITickableSound
import net.minecraft.client.audio.PositionedSound
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
class MovingEntitySound(
    private val entity: Entity,
    id: ResourceLocation,
    category: SoundCategory,
    volume: Float,
    pitch: Float
) : PositionedSound(id,category), ITickableSound {

    init {
        this.volume = volume
        this.pitch = pitch
    }

    private var finished = false
    override fun isDonePlaying() = finished

    override fun update() {
        if (entity.isDead) {
            finished = true
            return
        }

        this.xPosF = entity.posX.toFloat()
        this.yPosF = entity.posY.toFloat()
        this.zPosF = entity.posZ.toFloat()
    }
}