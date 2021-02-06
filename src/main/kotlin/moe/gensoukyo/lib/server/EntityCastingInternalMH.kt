@file:SideOnly(Side.SERVER)

package moe.gensoukyo.lib.server

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import net.minecraft.entity.Entity as McEntity
import org.bukkit.entity.Entity as BukkitEntity

internal val mc2Bukkit = MethodHandles.lookup().unreflect(
    McEntity::class.java.methods.firstOrNull {
        it.parameterCount == 0 &&
                BukkitEntity::class.java.isAssignableFrom(it.returnType)
    } ?: McEntity::class.java.getMethod("getBukkitEntity")
)

internal var bukkit2MC: MethodHandle? = null
internal fun initBukkit2McHandle(e: BukkitEntity) {
    if (bukkit2MC != null) {
        return
    }
    bukkit2MC = MethodHandles.lookup().unreflect(
        e.javaClass.methods.firstOrNull {
            it.parameterCount == 0 &&
                    McEntity::class.java.isAssignableFrom(it.returnType)
        } ?: e.javaClass.getMethod("getHandle")
        ?: throw UnsupportedOperationException()
    )
}