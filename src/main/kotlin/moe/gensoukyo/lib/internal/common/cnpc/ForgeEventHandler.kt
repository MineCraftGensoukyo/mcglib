package moe.gensoukyo.lib.internal.common.cnpc

import moe.gensoukyo.lib.MCGLib
import moe.gensoukyo.lib.constants.ModIds
import moe.gensoukyo.lib.scripting.runScript
import net.minecraftforge.event.entity.EntityEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Optional
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
 * @author ChloePrime
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
    @Optional.Method(modid = ModIds.CNPC)
    fun onForgeEvent(e: Event) {
        if (FMLCommonHandler.instance().effectiveSide.isClient) return
        if (!isEventInWhitelist(e)) return

        if (BLACKLIST.any { clazz -> clazz.isInstance(e) }) return

        runForgeEventUnchecked(e)
    }

    /**
     * Whitelist config won't work with this.
     */
    internal fun runForgeEventUnchecked(e: Event) {
        ScriptController.Instance?.forgeScripts?.runScript(e)
    }
}