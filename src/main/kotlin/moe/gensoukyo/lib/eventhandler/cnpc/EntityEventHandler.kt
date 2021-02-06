package moe.gensoukyo.lib.eventhandler.cnpc

import moe.gensoukyo.lib.MCGLib
import moe.gensoukyo.lib.internal.runForgeScript
import moe.gensoukyo.lib.server.bukkit
import moe.gensoukyo.lib.server.npcApi
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.EntityEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import noppes.npcs.controllers.data.PlayerData
import noppes.npcs.entity.EntityNPCInterface
import net.minecraftforge.fml.common.gameevent.PlayerEvent as FMLPlayerEvent

/**
 * Transmitting forge (entity) events to script
 * 建议拿到 MC 的 Entity 后使用 [bukkit] 扩展属性而不是 [npcApi].
 *
 * @see ForgeEventHandler
 */
@Mod.EventBusSubscriber(modid = MCGLib.MODID)
object EntityEventHandler {
    @JvmStatic
    @SubscribeEvent
    fun onEntityEvent(e: EntityEvent) {
        val entity = e.entity
        if (entity.world.isRemote) return
        if (entity is EntityPlayer) {
            runPlayerEvent(entity, e)
        } else if (entity is EntityNPCInterface) {
            runNpcEvent(entity, e)
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onFMLPlayerEvent(e: FMLPlayerEvent) {
        runPlayerEvent(e.player, e)
    }

    private fun runPlayerEvent(player: EntityPlayer, event: Event) {
        if (player.world.isRemote) return
        PlayerData.get(player).scriptData?.runForgeScript(event)
    }

    private fun runNpcEvent(npc: EntityNPCInterface, event: Event) {
        if (npc.world.isRemote) return
        npc.script?.runForgeScript(event)
    }
}