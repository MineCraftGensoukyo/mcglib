package moe.gensoukyo.lib.internal.cnpc

import moe.gensoukyo.lib.constants.ModIds
import moe.gensoukyo.lib.internal.forgeEventHooks
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.common.eventhandler.Event
import noppes.npcs.controllers.IScriptHandler

@Optional.Method(modid = ModIds.CNPC)
internal fun IScriptHandler.runForgeScript(e: Event) {
    forgeEventHooks[e.javaClass].forEach { hook ->
        this.runScript(hook, e)
    }
}