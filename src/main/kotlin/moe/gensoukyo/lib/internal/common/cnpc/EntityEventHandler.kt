package moe.gensoukyo.lib.internal.common.cnpc

import moe.gensoukyo.lib.MCGLib
import moe.gensoukyo.lib.constants.ModIds
import moe.gensoukyo.lib.scripting.runScript
import moe.gensoukyo.lib.server.bukkit
import moe.gensoukyo.lib.util.cnpc.npcApi
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.EntityEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import noppes.npcs.controllers.data.PlayerData
import noppes.npcs.entity.EntityNPCInterface
import java.lang.Exception
import net.minecraftforge.fml.common.gameevent.PlayerEvent as FMLPlayerEvent

/**
 * Transmitting forge (entity) events to script
 * 建议拿到 MC 的 Entity 后使用 [bukkit] 扩展属性而不是 [npcApi].
 *
 * @see ForgeEventHandler
 * @author ChloePrime
 */
@Mod.EventBusSubscriber(modid = MCGLib.MODID)
object EntityEventHandler {
    private val badEventTypes = HashSet<Class<out Event>>()

    @JvmStatic
    @SubscribeEvent
    @Optional.Method(modid = ModIds.CNPC)
    fun onEntityEvent(e: EntityEvent) {
        val entity = e.entity ?: return
        if (entity.world?.isRemote != false) return
        if (!isEventInWhitelist(e)) return

        when (entity) {
            is EntityPlayer -> {
                runPlayerEvent(entity, e)
            }
            is EntityNPCInterface -> {
                runNpcEvent(entity, e)
            }
        }
        ForgeEventHandler.runForgeEventUnchecked(e)
    }

    @JvmStatic
    @SubscribeEvent
    @Optional.Method(modid = ModIds.CNPC)
    fun onFMLPlayerEvent(e: FMLPlayerEvent) {
        if (!isEventInWhitelist(e)) return
        runPlayerEvent(e.player ?: return, e)
        ForgeEventHandler.runForgeEventUnchecked(e)
    }

    private fun runPlayerEvent(player: EntityPlayer, event: Event) {
        if (event.javaClass in badEventTypes) return

        if (player.world.isRemote) return
        try {
            PlayerData.get(player).scriptData?.runScript(event)
        } catch (e: Exception) {
            MCGLib.getLogger().info("Bad event type: ${event.javaClass.canonicalName}")
            MCGLib.getLogger().debug("Stacktrace:", e)
            badEventTypes.add(event.javaClass)
        }
    }

    private fun runNpcEvent(npc: EntityNPCInterface, event: Event) {
        if (npc.world.isRemote) return
        npc.script?.runScript(event)
    }
}