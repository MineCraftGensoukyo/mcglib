package moe.gensoukyo.lib.scripting

import com.google.common.annotations.Beta

/**
 * 自动关闭旧值的 TempData
 */
open class AutoClosingHashMap<K, V: AutoCloseable>: HashMap<K, V>() {
    override fun put(key: K, value: V): V? {
        val prev = super.put(key, value)
        prev?.close()
        return prev
    }

    override fun putAll(from: Map<out K, V>) {
        from.forEach { (key, value) ->
            put(key, value)
        }
    }

    override fun putIfAbsent(key: K, value: V): V? {
        val prev = super.putIfAbsent(key, value)
        prev?.close()
        return prev
    }

    override fun remove(key: K): V? {
        val prev = super.remove(key)
        prev?.close()
        return prev
    }

    override fun clear() {
        values.forEach { it.close() }
        super.clear()
    }
}

@Beta
object AutoClosingTempData: AutoClosingHashMap<String, AutoCloseable>()