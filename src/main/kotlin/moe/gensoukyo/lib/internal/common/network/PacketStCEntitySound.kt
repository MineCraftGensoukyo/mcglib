package moe.gensoukyo.lib.internal.common.network

import io.netty.buffer.ByteBuf
import moe.gensoukyo.lib.internal.client.audio.MovingEntitySound
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class PacketStCEntitySound: Packet {
    var entityId = 0
    var sound = "minecraft:ui.button.click"
    var category = SoundCategory.MASTER
    var volume = 0F
    var pitch = 0F

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    override fun fromBytes(buf: ByteBuf) {
        sound = ByteBufUtils.readUTF8String(buf)
        category = SoundCategory.values()[buf.readByte().toInt()]
        entityId = buf.readInt()
        volume = buf.readFloat()
        pitch = buf.readFloat()
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     * @param buf
     */
    override fun toBytes(buf: ByteBuf) {
        ByteBufUtils.writeUTF8String(buf, sound)
        buf.writeByte(category.ordinal)
        buf.writeInt(entityId)
        buf.writeFloat(volume)
        buf.writeFloat(pitch)
    }

    @SideOnly(Side.CLIENT)
    override fun handle(ctx: MessageContext) {
        Minecraft.getMinecraft().addScheduledTask {
            val mc = Minecraft.getMinecraft()
            val entity = mc.world.getEntityByID(entityId) ?: return@addScheduledTask

            val sound = MovingEntitySound(entity, ResourceLocation(this.sound), category, volume, pitch)
            mc.soundHandler.playSound(sound)
        }
    }
}