package moe.gensoukyo.lib.item


import net.minecraft.item.ItemStack as MCItemStack
import org.bukkit.inventory.ItemStack as BukkitItemStack

private val CraftItemStack = Class.forName("CraftItemStack")
val asNMSCopy = CraftItemStack.getMethod("asNMSCopy",null,BukkitItemStack::class.java)
val asBukkitCopy = CraftItemStack.getMethod("asBukkitCopy",null,MCItemStack::class.java)
val BukkitItemStack.mcItemStack: MCItemStack
    inline get() = asNMSCopy.invoke(this) as MCItemStack
val MCItemStack.bukkit: BukkitItemStack
    inline get() = asBukkitCopy.invoke(this) as BukkitItemStack