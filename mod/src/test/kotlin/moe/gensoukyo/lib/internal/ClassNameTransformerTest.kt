package moe.gensoukyo.lib.internal

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ClassNameTransformerTest {
    class Foo

    @Test
    fun testToLowerCamelCase() {
        Assertions.assertEquals(
            "javaLangObject",
            ClassNameTransformer.toLowerCamelCase(Object::class.java)
        )
        Assertions.assertEquals(
            "moeGensoukyoLibInternalClassNameTransformerTest_Foo",
            ClassNameTransformer.toLowerCamelCase(Foo::class.java)
        )
    }
}