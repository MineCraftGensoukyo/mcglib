package moe.gensoukyo.lib.reflection

import noppes.npcs.api.event.BlockEvent

object Lalala {
    @JvmStatic
    fun interact(e: BlockEvent.InteractEvent) {
        e.player.message("啦啦啦")
    }

    @JvmStatic
    private fun mySecret(e: BlockEvent.InteractEvent) {
        e.player.message("恭喜你发现了我的秘密")
    }
}

fun result() = Lalala::class.java