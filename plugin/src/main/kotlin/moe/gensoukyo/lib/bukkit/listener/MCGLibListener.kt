package moe.gensoukyo.lib.bukkit.listener

import io.izzel.taboolib.module.inject.TListener
import kotlin.jvm.javaClass
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityEvent

@TListener
class MCGLibListener: Listener {
    @EventHandler
    fun onEvent(e: EntityEvent) {
    }

    private val <T: Any> T.isBukkitObject: Boolean get() {
        return this::class.java.name
            .startsWith("org.bukkit.")
    }
}