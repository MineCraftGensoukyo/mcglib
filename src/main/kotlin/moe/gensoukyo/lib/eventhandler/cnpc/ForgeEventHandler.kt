package moe.gensoukyo.lib.eventhandler.cnpc

import moe.gensoukyo.lib.MCGLib
import moe.gensoukyo.lib.internal.runForgeScript
import net.minecraftforge.event.entity.EntityEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import noppes.npcs.controllers.ScriptController
import net.minecraftforge.fml.common.gameevent.PlayerEvent as FMLPlayerEvent

/**
 * Transmitting forge (world) events to script
 * The name of the hook is renamed to lowerCamelCase with dots removed.
 *
 * How to use: (in this example, the class is java.lang.FishEvent)
 * js:
 * ---
 * function javaLangFishEvent(e) {...}
 * ---
 *
 * kotlin:
 * ---
 * import java.lang.FishEvent
 * fun javaLangFishEvent(e: FishEvent) {...}
 * ---
 *
 * @see ForgeEventHandler
 */
@Mod.EventBusSubscriber(modid = MCGLib.MODID)
object ForgeEventHandler {
    private val BLACKLIST = arrayOf(
        BlockEvent::class.java,
        EntityEvent::class.java,
        FMLPlayerEvent::class.java
    )

    @JvmStatic
    @SubscribeEvent
    fun onForgeEvent(e: Event) {
        if (FMLCommonHandler.instance().effectiveSide.isClient) return
        if (BLACKLIST.any { clazz -> clazz.isInstance(e) }) return

        ScriptController.Instance?.forgeScripts?.runForgeScript(e)
    }
}