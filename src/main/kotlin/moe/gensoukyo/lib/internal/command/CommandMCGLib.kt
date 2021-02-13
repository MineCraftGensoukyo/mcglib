package moe.gensoukyo.lib.internal.command

import moe.gensoukyo.lib.MCGLib
import moe.gensoukyo.lib.ModConfig
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraftforge.common.config.Config
import net.minecraftforge.common.config.ConfigManager
import net.minecraftforge.server.command.CommandTreeBase

/**
 * @author ChloePrime
 */
class CommandMCGLib: CommandTreeBase() {
    companion object {
        private const val PERM_RELOAD = "${MCGLib.MODID}.reload.config"
    }

    init {
        addSubcommand(ReloadConfig())
    }

    override fun getName() = "mcglib"
    override fun getUsage(sender: ICommandSender) = "/mcglib <reloadConfig>"

    /**
     * Reload config command.
     * @author ChloePrime
     */
    private class ReloadConfig: PermissionNodeBasedCommand(PERM_RELOAD) {
        override fun getName() = "reloadConfig"

        override fun getUsage(sender: ICommandSender)
        = "/mcglib reloadConfig"

        override fun execute(
            server: MinecraftServer,
            sender: ICommandSender,
            args: Array<out String>
        ) {
            ModConfig.reload()
        }

    }
}