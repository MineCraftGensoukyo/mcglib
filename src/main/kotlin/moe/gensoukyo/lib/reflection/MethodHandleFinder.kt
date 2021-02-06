@file:Suppress("unused")

package moe.gensoukyo.lib.reflection

import java.lang.invoke.MethodHandle

fun Class<*>.getMethodHandle(
    name:String,
    vararg args:Class<*>
): MethodHandle
= MethodHandleFinder.getMethod(this, name, *args)

fun Class<*>.getDeclaredMethodHandle(
    name:String,
    vararg args:Class<*>
): MethodHandle
= MethodHandleFinder.getDeclaredMethod(this, name, *args)