package moe.gensoukyo.lib.internal.common.command

import moe.gensoukyo.lib.MCGLib
import moe.gensoukyo.lib.item.ItemSerialization
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.server.command.CommandTreeBase

/**
 * @author ChloePrime
 */
class CommandSerialItem: CommandTreeBase() {
    companion object {
        const val PERM_GET = "${MCGLib.MODID}.items.get"
        const val PERM_PUT = "${MCGLib.MODID}.items.put"
        const val PERM_RELOAD = "${MCGLib.MODID}.items.reload"
    }

    init {
        addSubcommand(Get())
        addSubcommand(Put())
        addSubcommand(Reload())
    }

    override fun getName() = "itemSerial"

    override fun getUsage(sender: ICommandSender) = "/itemSerial <get|put>"

    /**
     * Get Command
     */
    private class Get : PermissionNodeBasedCommand(PERM_GET) {
        override fun getName() = "get"
        override fun getUsage(sender: ICommandSender)
        = "/itemSerial get <(itemPath)>"

        override fun execute(
            server: MinecraftServer,
            sender: ICommandSender,
            args: Array<String>
        ) {
            // Preconditions
            if (args.isEmpty()) {
                notifyTooFewArgs(sender, 1.toString(), args.size)
                return
            }

            // Do it
            val item = ItemSerialization.get(args[0])
            if (sender is EntityPlayer) {
                sender.addItemStackToInventory(item)
                return
            }

            if (sender !is ICapabilityProvider) {
                return
            }
            sender.getCapability(
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null
            )?.apply {
                for (i in 0 until this.slots) {
                    if (!getStackInSlot(i).isEmpty) continue
                    insertItem(i, item, false)
                    break
                }
            }
        }

    }

    /**
     * Put Command.
     * Only players can preform this command
     * The item is retrieved from the player's main hand.
     */
    private class Put: PermissionNodeBasedCommand(PERM_PUT) {
        override fun getName() = "put"

        override fun getUsage(sender: ICommandSender)
        = "/itemSerial put <(itemPath)>"

        override fun execute(
            server: MinecraftServer,
            sender: ICommandSender,
            args: Array<out String>
        ) {
            // Preconditions
            if (sender !is EntityPlayer) {
                sender.sendMessage(
                    TextComponentTranslation(LANG_COMMAND_PLAYER_ONLY)
                )
                return
            }
            if (args.isEmpty()) {
                notifyTooFewArgs(sender, 1.toString(), args.size)
                return
            }

            // Do it
            try {
                ItemSerialization.put(args[0], sender.heldItemMainhand)
            } catch (ex: Exception) {
                sender.sendMessage(
                    TextComponentTranslation(LANG_COMMAND_ERROR)
                )
                MCGLib.getLogger().warn("Error in item serialization", ex)
            }
        }

    }

    /**
     * Reload Command
     */
    private class Reload : PermissionNodeBasedCommand(PERM_RELOAD) {
        override fun getName() = "reload"
        override fun getUsage(sender: ICommandSender) = "/itemSerial reload"

        override fun execute(
            server: MinecraftServer,
            sender: ICommandSender,
            args: Array<String>
        ) = ItemSerialization.reload()

    }
}