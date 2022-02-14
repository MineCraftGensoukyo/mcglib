package moe.gensoukyo.lib.scripting

import moe.gensoukyo.lib.internal.HookPool
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import noppes.npcs.controllers.IScriptHandler
import noppes.npcs.controllers.data.ForgeScriptData
import noppes.npcs.controllers.data.PlayerData
import noppes.npcs.entity.EntityNPCInterface
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
fun IScriptHandler.runScript(event: Any) {
    this.runScript(ScriptParameter(event), event.javaClass)
}

fun IScriptHandler.runScript(event: ForgeEvent) {
    this.runScript(event, event.javaClass)
}

fun Entity.runScriptIfIsNpc(event: Any) {
    if (this.world.isRemote || this !is EntityNPCInterface) return
    this.script?.runScript(event)
}

/**
 * Run player script.
 */
fun EntityPlayer.runPlayerScript(event: Any) {
    if (this.world.isRemote) return
    PlayerData.get(this)?.scriptData?.runScript(event)
}

/**
 * @param clazz the clazz that will be used to generate script hook.
 */
private fun IScriptHandler.runScript(event: ForgeEvent, clazz: Class<*>) {
    if (this is ForgeScriptData) {
        HookPool[clazz].forEach { hook ->
            this.runScript(hook.function, event)
        }
    } else {
        HookPool[clazz].forEach { hook ->
            this.runScript(hook, event)
        }
    }
}
