package moe.gensoukyo.lib.internal

import net.minecraftforge.fml.common.eventhandler.Event
import noppes.npcs.controllers.IScriptHandler

internal fun IScriptHandler.runForgeScript(e: Event) {
    forgeEventHooks[e.javaClass].forEach { hook ->
        this.runScript(hook, e)
    }
}