package moe.gensoukyo.lib.internal

object ClassNameTransformer {
    /**
     * 将类名转换为变量名
     * 去除点，并将点后的字母大写
     * "java.lang.Fish" => "javaLangFish"，
     *
     * 对于内部类，最后一个.将被替换为_
     */
    fun toLowerCamelCase(clazz: Class<*>): String {
        var isDot = false
        val sb = StringBuilder()
        var className = clazz.canonicalName
        if (clazz.enclosingClass != null) {
            className = className.reversed().replaceFirst('.', '_').reversed()
        }
        for (char in className) {
            if (isDot) {
                sb.append(char.toUpperCase())
                isDot = false
                continue
            }
            if (char == '.') {
                isDot = true
                continue
            }
            sb.append(char)
        }
        return sb.toString()
    }
}