@file:Suppress("unused")

package moe.gensoukyo.lib.util

import java.util.stream.Stream

inline fun <reified T> Stream<*>.cast(): Stream<T> =
    filter {
        it is T
    }.map {
        it as T
    }