package moe.gensoukyo.lib.internal.common.util

import javax.annotation.Nonnull
import javax.annotation.Nullable

/**
 * Fake [Nonnull] -> [Nullable]
 */
internal fun <T: Any> distrust(o: T?): T? = o