@file:Suppress("Unused")

/**
 * DataToken的语法糖
 */
package moe.gensoukyo.lib.reflection

import moe.gensoukyo.lib.constants.ModIds
import moe.gensoukyo.lib.maps.DataToken
import net.minecraftforge.fml.common.Optional
import noppes.npcs.api.entity.data.IData

// CNPC IData

@Optional.Method(modid = ModIds.CNPC)
fun <T> IData.put(key: DataToken<T>, value: T) {
    key.put(this, value)
}

operator fun <T> IData.set(key: DataToken<T>, value: T) = this.put(key, value)

@Optional.Method(modid = ModIds.CNPC)
operator fun <T> IData.get(key: DataToken<T>): T {
    return key.get(this)
}

@Optional.Method(modid = ModIds.CNPC)
fun <T> IData.remove(key: DataToken<T>) {
    key.clear(this)
}

@Optional.Method(modid = ModIds.CNPC)
fun <T> IData.has(key: DataToken<T>): Boolean {
    return key.peek(this) != null
}

// Plain Java Maps

fun <T> Map<String, Any>.put(key: DataToken<T>, value: T) {
    key.put(this, value)
}

operator fun <T> Map<String, Any>.set(key: DataToken<T>, value: T) = this.put(key, value)

operator fun <T> Map<String, Any>.get(key: DataToken<T>): T {
    return key.get(this)
}

fun <T> Map<String, Any>.remove(key: DataToken<T>) {
    key.clear(this)
}
fun <T> Map<String, Any>.has(key: DataToken<T>): Boolean {
    return key.peek(this) != null
}