package moe.gensoukyo.lib.internal.common.network

import moe.gensoukyo.lib.MCGLib
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.relauncher.Side

object MCGLibNetworkManager {
    private const val CHANNEL_NAME = MCGLib.MODID
    @JvmField
    val CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL_NAME)!!

    @JvmStatic
    fun init() {
        register<PacketStCEntitySound>(Side.CLIENT)
    }

    private var counter = 0
    private inline fun <reified P: Packet> register(target: Side) {
        CHANNEL.registerMessage(handlerClass(), P::class.java, counter++, target)
    }
}