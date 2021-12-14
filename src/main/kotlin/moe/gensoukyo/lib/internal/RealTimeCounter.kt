package moe.gensoukyo.lib.internal

import kotlin.math.max

class RealTimeCounter(
    private val minimumDeltaTime: Float
) {
    var deltaRealTime = 0f
        private set

    private var lastMillis = -1L

    fun refresh(): RealTimeCounter {
        val cur = System.currentTimeMillis()
        val last = lastMillis

        lastMillis = cur
        deltaRealTime = if (last < 0) {
            minimumDeltaTime
        } else {
            max(minimumDeltaTime, (cur - last) / 1000F)
        }

        return this
    }
}