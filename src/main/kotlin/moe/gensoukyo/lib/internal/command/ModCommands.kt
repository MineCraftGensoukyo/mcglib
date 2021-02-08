package moe.gensoukyo.lib.internal.command

import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.server.permission.DefaultPermissionLevel
import net.minecraftforge.server.permission.PermissionAPI

internal fun initPermissions() {
    PermissionAPI.registerNode(
        CommandReloadItemPool.PERM_NODE,
        DefaultPermissionLevel.OP,
        "Permission to reload the item pool, by clearing its cache."
    )
}

internal fun registerCommands(e: FMLServerStartingEvent) {
    e.registerServerCommand(CommandMCGLib())
}