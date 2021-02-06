package moe.gensoukyo.lib.reflection

import moe.gensoukyo.kotlin.pool.KotlinPool
import noppes.npcs.api.event.BlockEvent
import java.io.PrintWriter
import java.io.StringWriter

fun interact(e: BlockEvent.InteractEvent) {
    val clazz = KotlinPool["moe.gensoukyo.lib.reflection.Lalala"]!!.representedClass
    try {
        clazz.getMethodHandle("interact").invoke(e)
        clazz.getDeclaredMethodHandle("mySecret").invoke(e)
    } catch (ex: Exception) {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        ex.printStackTrace(pw)
        e.block.world.broadcast(sw.toString())
    }
}