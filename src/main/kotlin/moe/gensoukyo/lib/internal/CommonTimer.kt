package moe.gensoukyo.lib.internal

import moe.gensoukyo.lib.MCGLib
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@Mod.EventBusSubscriber(modid = MCGLib.MODID)
object CommonTimer {
    internal var fixedDeltaRealTime = 0f
    private val timer = RealTimeCounter(1 / 20F)

    @JvmStatic
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onWorldTick(e: TickEvent.ServerTickEvent) {
        if (e.phase == TickEvent.Phase.END) return
        fixedDeltaRealTime = timer.refresh().deltaRealTime
    }
}
