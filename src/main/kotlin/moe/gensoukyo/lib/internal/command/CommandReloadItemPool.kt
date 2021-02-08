package moe.gensoukyo.lib.internal.command

import moe.gensoukyo.lib.item.ItemSerialization
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraftforge.server.command.CommandTreeBase
import net.minecraftforge.server.permission.PermissionAPI

class CommandReloadItemPool: CommandBase() {
    companion object {
        const val PERM_NODE = "mcglib.reload.items"
    }

    override fun getName() = "reloadItems"

    override fun getUsage(sender: ICommandSender)
    = "/mcglib reloadItems"

    override fun checkPermission(
        server: MinecraftServer,
        sender: ICommandSender
    ): Boolean {
        if (sender !is EntityPlayer) return true
        return PermissionAPI.hasPermission(sender, PERM_NODE)
    }

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>)
    = ItemSerialization.reload()

}