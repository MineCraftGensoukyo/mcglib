package moe.gensoukyo.lib.timer

import moe.gensoukyo.lib.internal.CommonTimer
import moe.gensoukyo.lib.internal.client.ClientTimer
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object Time {
    private val fml = FMLCommonHandler.instance()

    /**
     * moe.gensoukyo.lib.timer.Time.useRealTime
     */
    private val useRealTime =
        java.lang.Boolean.getBoolean("${javaClass.name}.useRealTime")

    /**
     * Time.deltaTime calculated from partial ticks.
     * see https://docs.unity3d.com/ScriptReference/Time-deltaTime.html
     */
    val deltaTime
        get() = if (fml.side == Side.CLIENT && fml.effectiveSide == Side.CLIENT) {
            getDeltaRenderTime()
        } else {
            fixedDeltaTime
        }

    val fixedDeltaTime
        get() = if (useRealTime) {
            CommonTimer.fixedDeltaRealTime
        } else 1 / 20F

    @SideOnly(Side.CLIENT)
    private fun getDeltaRenderTime() =
        if (useRealTime) {
            ClientTimer.deltaRealTime
        } else {
            ClientTimer.deltaTime
        }
}
