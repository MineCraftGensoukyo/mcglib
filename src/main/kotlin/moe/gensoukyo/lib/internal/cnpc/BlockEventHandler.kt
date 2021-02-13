package moe.gensoukyo.lib.internal.cnpc

import moe.gensoukyo.lib.MCGLib
import moe.gensoukyo.lib.constants.ModIds
import moe.gensoukyo.lib.scripting.runScript
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import noppes.npcs.controllers.IScriptHandler
import noppes.npcs.controllers.ScriptController

/**
 * Transmitting forge (block) events to script
 *
 * @see ForgeEventHandler
 * @author ChloePrime
 */
@Mod.EventBusSubscriber(modid = MCGLib.MODID)
object BlockEventHandler {
    @JvmStatic
    @SubscribeEvent
    @Optional.Method(modid = ModIds.CNPC)
    fun onBlockEvent(e: BlockEvent) {
        if (e.world?.isRemote != false) return
        if (!isEventInWhitelist(e)) return

        val block = e.world.getTileEntity(e.pos) ?: return
        if (block is IScriptHandler) {
            block.runScript(e)
        }
    }

    @JvmStatic
    @SubscribeEvent
    @Optional.Method(modid = ModIds.CNPC)
    fun onWorldEvent(e: WorldEvent) {
        if (e.world.isRemote) return
        if (!isEventInWhitelist(e)) return

        ScriptController.Instance?.forgeScripts?.runScript(e)
    }
}