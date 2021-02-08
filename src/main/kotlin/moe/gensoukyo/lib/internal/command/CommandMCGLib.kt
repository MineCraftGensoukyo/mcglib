package moe.gensoukyo.lib.internal.command

import net.minecraft.command.ICommandSender
import net.minecraftforge.server.command.CommandTreeBase

class CommandMCGLib: CommandTreeBase() {
    override fun getName() = "mcglib"

    override fun getUsage(sender: ICommandSender)
    = "/mcglib <reloadItems>"

}