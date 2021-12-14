package moe.gensoukyo.lib.util.cnpc

import noppes.npcs.api.event.ItemEvent
import noppes.npcs.api.item.IItemStack

var IItemStack.foo by tempdata { 0L }

fun interact(e: ItemEvent.InteractEvent) {
    e.item.foo
    e.player.message("${e.item.getTempdataPlain("foo")}")
    e.item.foo = 42L
    e.player.message("${e.item.getTempdataPlain("foo")}")
    e.item.foo = 84L
    e.player.message("${e.item.getTempdataPlain("foo")}")
}

fun IItemStack.getTempdataPlain(name: String): Any? {
    return this.tempdata[name]
}