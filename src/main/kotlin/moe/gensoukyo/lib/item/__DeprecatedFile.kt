/**
 * DEPRECATED
 */
@file:SideOnly(Side.SERVER)
package moe.gensoukyo.lib.item

import moe.gensoukyo.lib.server.bukkit
import moe.gensoukyo.lib.server.mcItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraft.item.ItemStack as MCItemStack
import org.bukkit.inventory.ItemStack as BukkitItemStack

@Deprecated(
    "使用moe.gensoukyo.lib.server内的方法",
    ReplaceWith("this.mcItemStack", "moe.gensoukyo.lib.server.mcItemStack")
)
val BukkitItemStack.mcItemStack: MCItemStack
    get() = this.mcItemStack

@Deprecated(
    "使用moe.gensoukyo.lib.server内的方法",
    ReplaceWith("this.bukkit", "moe.gensoukyo.lib.server.bukkit")
)
val MCItemStack.bukkit: BukkitItemStack
    get() = this.bukkit
