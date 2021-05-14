package moe.gensoukyo.lib.scripting.testing;

import noppes.npcs.api.entity.IPlayer
import java.lang.reflect.Method

annotation class Test

private class TestInfo(
    val methods: Array<Method>,
    var cursor: Int = 0
)

private val testInfoHolder = object: ClassValue<TestInfo>() {
    override fun computeValue(type: Class<*>): TestInfo {
        val methods = type.methods.filter {
            it.isAnnotationPresent(Test::class.java)
        }.toTypedArray()
        return TestInfo(methods)
    }
}

fun doScriptTest(thiz: Any, operator: IPlayer<*>) {
    val testInfo = testInfoHolder[thiz.javaClass]
    val methodCount = testInfo.methods.size
    if (methodCount == 0) {
        return
    }
    val method = testInfo.methods[testInfo.cursor]
    operator.message("Start §6${method.name}()")
    try {
        method(thiz, operator)
        operator.message("§aTest  PASSED")
    } catch (e: Exception) {
        operator.message("§cTest  FAILED\n§4reason: ${e.message}")
    }
    testInfo.cursor = (testInfo.cursor + 1) % methodCount
}
