package moe.gensoukyo.lib.item


import noppes.npcs.api.NpcAPI
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import net.minecraft.item.ItemStack as MCItemStack
import org.bukkit.inventory.ItemStack as BukkitItemStack


private val CraftItemStack = Class.forName("org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack")
val asNMSCopy:MethodHandle = MethodHandles.lookup().unreflect(
    CraftItemStack.methods.first{ it.name=="asNMSCopy" }
)
val asBukkitCopy:MethodHandle = MethodHandles.lookup().unreflect(
    CraftItemStack.methods.first{ it.name=="asBukkitCopy" }
)
val BukkitItemStack.mcItemStack: MCItemStack
    get() = asNMSCopy.invoke(this) as MCItemStack
val MCItemStack.bukkit: BukkitItemStack
    get() = asBukkitCopy.invoke(this) as BukkitItemStack