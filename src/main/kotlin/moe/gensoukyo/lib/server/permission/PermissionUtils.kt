@file:Suppress("unused")

package moe.gensoukyo.lib.server.permission

import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher.Side
import noppes.npcs.api.entity.IPlayer

/**
 * 给予临时op
 */
inline fun IPlayer<*>.withOp(block: IPlayer<*>.() -> Unit) {
    if (FMLCommonHandler.instance().side == Side.CLIENT) {
        block()
        return
    }
    val playerManager = this.mcEntity.server?.playerList ?: return
    val profile = this.mcEntity.gameProfile
    val isOpBefore = playerManager.canSendCommands(profile)

    if (!isOpBefore) {
        playerManager.addOp(profile)
    }
    try {
        block()
    } finally {
        if (!isOpBefore) {
            playerManager.removeOp(profile)
        }
    }
}

fun IPlayer<*>.executeCommand(cmd: String): Int {
    return this.mcEntity.server?.commandManager?.executeCommand(
        this.mcEntity, cmd
    ) ?: 0
}
