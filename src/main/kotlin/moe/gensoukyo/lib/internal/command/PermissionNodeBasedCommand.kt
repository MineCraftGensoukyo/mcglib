package moe.gensoukyo.lib.internal.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraftforge.server.permission.PermissionAPI

abstract class PermissionNodeBasedCommand(
    private val permission: String
): CommandBase() {
    override fun checkPermission(
        server: MinecraftServer,
        sender: ICommandSender
    ): Boolean {
        if (sender !is EntityPlayer) return true
        return PermissionAPI.hasPermission(sender, permission)
    }
}