package moe.gensoukyo.lib.internal.common.network

import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

interface Packet : IMessage {
    fun handle(ctx: MessageContext) {}
}

internal class PacketHandler<P : Packet> : IMessageHandler<P, IMessage?> {
    override fun onMessage(message: P, ctx: MessageContext): IMessage? {
        message.handle(ctx)
        return null
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <P : Packet> handlerClass(): Class<PacketHandler<P>> {
    return PacketHandler::class.java as Class<PacketHandler<P>>
}