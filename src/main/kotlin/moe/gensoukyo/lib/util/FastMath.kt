@file:Suppress("unused")

package moe.gensoukyo.lib.util

import javax.vecmath.Vector3d
import javax.vecmath.Vector3f
import kotlin.math.sqrt

fun invSqrt (x: Float)  = FastMath.invSqrt(x)
fun invSqrt (x: Double) = FastMath.invSqrt(x)
fun fastSqrt(x: Float)  = FastMath.sqrt(x)
fun fastSqrt(x: Double) = FastMath.sqrt(x)

fun Vector3f.fastNormalize() {
    val norm = invSqrt(x * x + y * y + z * z)
    x *= norm
    y *= norm
    z *= norm
}

fun Vector3d.fastNormalize() {
    val norm = invSqrt(x * x + y * y + z * z)
    x *= norm
    y *= norm
    z *= norm
}

val Vector3f.length: Float get() {
    return sqrt(x * x + y * y + z * z)
}

val Vector3d.length: Double get() {
    return sqrt(x * x + y * y + z * z)
}