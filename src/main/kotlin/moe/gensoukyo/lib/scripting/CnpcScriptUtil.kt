package moe.gensoukyo.lib.scripting

import moe.gensoukyo.lib.internal.HookPool
import noppes.npcs.controllers.IScriptHandler
import net.minecraftforge.fml.common.eventhandler.Event as ForgeEvent

/**
 * Run CNPC script.
 * The forge event whitelist will not be considered by this method.
 *
 * If the parameter [event] is not an instance of [ForgeEvent],
 * then it will be wrapped by the [ScriptParameter].
 *
 * @author ChloePrime
 */
@Suppress("unused")
fun IScriptHandler.runScript(event: Any) {
    this.runScript(ScriptParameter(event), event.javaClass)
}

fun IScriptHandler.runScript(event: ForgeEvent) {
    this.runScript(event, event.javaClass)
}

/**
 * @param clazz the clazz that will be used to generate script hook.
 */
private fun IScriptHandler.runScript(event: ForgeEvent, clazz: Class<*>) {
    HookPool[clazz].forEach { hook ->
        this.runScript(hook, event)
    }
}
