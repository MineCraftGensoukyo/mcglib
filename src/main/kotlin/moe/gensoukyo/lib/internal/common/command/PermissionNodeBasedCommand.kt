package moe.gensoukyo.lib.internal.common.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.server.permission.PermissionAPI

abstract class PermissionNodeBasedCommand(
    private val permission: String
): CommandBase() {
    override fun checkPermission(
        server: MinecraftServer,
        sender: ICommandSender
    ): Boolean {
        if (sender !is EntityPlayer) return true
        // 在物理客户端下，以世界是否允许作弊作为鉴权依据，而不是权限节点。
        if (FMLCommonHandler.instance().side.isClient) {
            return sender.world.worldInfo.areCommandsAllowed()
        }
        return PermissionAPI.hasPermission(sender, permission)
    }
}