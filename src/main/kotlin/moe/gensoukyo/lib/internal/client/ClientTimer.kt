package moe.gensoukyo.lib.internal.client

import moe.gensoukyo.lib.MCGLib
import moe.gensoukyo.lib.internal.RealTimeCounter
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.abs

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = MCGLib.MODID)
object ClientTimer {
    internal var deltaTime: Float = 0.0f
    internal var deltaRealTime: Float = 0.0f

    private var realTimeTimer = RealTimeCounter(0f)

    @JvmStatic
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onRenderTick(e: TickEvent.RenderTickEvent) {
        if (e.phase == TickEvent.Phase.END) return

        deltaRealTime = realTimeTimer.refresh().deltaRealTime
        refresh()
    }

    private var lastIntTick = 0L
    private var lastPartial = 0f

    private var cachedPartial = 0f

    private fun refresh() {
        val partial = Minecraft.getMinecraft().renderPartialTicks
        //防止重复调用多次刷新
        if (abs(partial - cachedPartial) <= Float.MIN_VALUE) {
            return
        }
        cachedPartial = partial

        val tickBefore = lastIntTick
        val partialBefore = lastPartial
        refreshStartTime()
        deltaTime = ((lastIntTick - tickBefore) + (lastPartial - partialBefore)) / 20F
    }

    private fun refreshStartTime() {
        lastIntTick = Minecraft.getMinecraft().world?.totalWorldTime ?: return
        lastPartial = Minecraft.getMinecraft().renderPartialTicks
    }
}