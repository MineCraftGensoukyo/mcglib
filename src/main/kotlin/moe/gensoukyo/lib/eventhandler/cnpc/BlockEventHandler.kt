package moe.gensoukyo.lib.eventhandler.cnpc

import moe.gensoukyo.lib.MCGLib
import moe.gensoukyo.lib.constants.ModIds
import moe.gensoukyo.lib.internal.runForgeScript
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
 */
@Mod.EventBusSubscriber(modid = MCGLib.MODID)
object BlockEventHandler {
    @JvmStatic
    @SubscribeEvent
    @Optional.Method(modid = ModIds.CNPC)
    fun onBlockEvent(e: BlockEvent) {
        if (e.world.isRemote) return

        val block = e.world.getTileEntity(e.pos) ?: return
        if (block is IScriptHandler) {
            block.runForgeScript(e)
        }
    }

    @JvmStatic
    @SubscribeEvent
    @Optional.Method(modid = ModIds.CNPC)
    fun onWorldEvent(e: WorldEvent) {
        if (e.world.isRemote) return
        ScriptController.Instance?.forgeScripts?.runForgeScript(e)
    }
}