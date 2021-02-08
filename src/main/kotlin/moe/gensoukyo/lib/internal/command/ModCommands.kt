package moe.gensoukyo.lib.internal.command

import moe.gensoukyo.lib.MCGLib
import net.minecraft.command.ICommandSender
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.server.permission.DefaultPermissionLevel
import net.minecraftforge.server.permission.PermissionAPI

internal const val LANG_COMMAND_HEADER = "command.${MCGLib.MODID}"
internal const val LANG_COMMAND_TOO_FEW_ARGS = "$LANG_COMMAND_HEADER.too.few.args"
internal const val LANG_COMMAND_PLAYER_ONLY = "$LANG_COMMAND_HEADER.player.only"
internal const val LANG_COMMAND_ERROR = "$LANG_COMMAND_HEADER.error"

internal fun initPermissions() {
    PermissionAPI.registerNode(
        CommandSerialItem.PERM_GET,
        DefaultPermissionLevel.OP,
        "Permission to get items from the item pool"
    )
    PermissionAPI.registerNode(
        CommandSerialItem.PERM_PUT,
        DefaultPermissionLevel.OP,
        "Permission to put an item in the item pool."
    )
    PermissionAPI.registerNode(
        CommandSerialItem.PERM_RELOAD,
        DefaultPermissionLevel.OP,
        "Permission to reload the item pool, by clearing its cache."
    )
}

internal fun registerCommands(e: FMLServerStartingEvent) {
    e.registerServerCommand(CommandSerialItem())
}

internal fun notifyTooFewArgs(
    sender: ICommandSender,
    expected: String,
    found: Int
) {
    sender.sendMessage(
        TextComponentTranslation(
            LANG_COMMAND_TOO_FEW_ARGS, expected, found
        )
    )
}