package moe.gensoukyo.lib.server

import noppes.npcs.api.event.BlockEvent
import java.io.PrintWriter
import java.io.StringWriter

fun interact(e: BlockEvent.InteractEvent) {

    try {
        val bPlayer = e.player.bukkit
        bPlayer.sendMessage("From Bukkit!")
        bPlayer.npcApi.message("I'm back!")
    } catch (ex: Exception) {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        ex.printStackTrace(pw)
        e.player.message(sw.toString())
    }
}