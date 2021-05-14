package moe.gensoukyo.lib.scripting

import com.google.common.annotations.Beta
import java.io.Closeable

/**
 * Invoke [init] function in this class in its init hook function.
 */
@Beta
abstract class AutoCloseableScript: Closeable {
    /**
     * Close previous record.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun init() {
        AutoClosingTempData[javaClass.name] = this
    }

    /**
     * No exceptions
     */
    abstract override fun close()
}
