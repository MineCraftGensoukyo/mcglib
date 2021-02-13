package moe.gensoukyo.lib.internal.cnpc

import moe.gensoukyo.lib.MCGLib
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.Event
import java.util.*

/**
 * Transmitting forge (world) events to script
 * The name of the hook is renamed to lowerCamelCase with dots removed.
 *
 * How to use: (in this example, the class is java.lang.FishEvent)
 * js:
 * ---
 * function javaLangFishEvent(e) {...}
 * ---
 *
 * kotlin:
 * ---
 * import java.lang.FishEvent
 * fun javaLangFishEvent(e: FishEvent) {...}
 * ---
 *
 * @see ForgeEventWhitelist
 */
@Mod.EventBusSubscriber(modid = MCGLib.MODID)
object ForgeEventWhitelist {
    private val knownResults = WeakHashMap<Class<*>, Boolean>()
    private var classNames: Set<String> = emptySet()

    @JvmStatic
    fun isInWhiteList(clazz: Class<*>): Boolean {
        val knownResult = knownResults[clazz]
        if (knownResult != null) {
            return knownResult
        }

        var result = false
        //确认输入的 Class 的父类是否在白名单中
        var clazz2: Class<*>? = clazz
        while (clazz2 != null) {
            if (clazz2.canonicalName in classNames) {
                result = true
                break
            }
            clazz2 = clazz2.superclass
        }

        knownResults[clazz] = result
        return result
    }

    @JvmStatic
    fun isInWhiteList(obj: Any) = isInWhiteList(obj.javaClass)

    @JvmStatic
    fun acceptConfigData(classNamesFromConfig: Array<String>) {
        classNames = classNamesFromConfig.toSet()
        knownResults.clear()
    }
}

internal fun isEventInWhitelist(obj: Event): Boolean {
    return ForgeEventWhitelist.isInWhiteList(obj)
}
