package moe.gensoukyo.lib.internal

import moe.gensoukyo.lib.MCGLib
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fml.common.eventhandler.Event
import noppes.npcs.constants.EnumScriptType

/**
 * Stores CNPC script hooks by class
 *
 * Used by forge events
 * and bukkit events
 */
class HookPool(
    private val upperBound: Class<*>
): ClassValue<Array<EnumScriptType>>() {

    override fun computeValue(type: Class<*>): Array<EnumScriptType> {
        val myEnum = getEnum(type) ?: return arrayOf()
        val superman = type.superclass
        if (superman == null || !upperBound.isAssignableFrom(superman)) {
            return arrayOf()
        }
        val result = mutableListOf(myEnum)
        result.addAll(get(superman))
        return result.toTypedArray()
    }

    private fun getEnum(type: Class<*>): EnumScriptType? {
        if (!upperBound.isAssignableFrom(type)) return null

        return EnumHelper.addEnum(
            EnumScriptType::class.java,
            "${MCGLib.MODID}:FORGE_ENTITY_EVENT${type.canonicalName.toUpperCase()}",
            arrayOf(String::class.java),
            ClassNameTransformer.toLowerCamelCase(type)
        )
    }
}

internal val forgeEventHooks = HookPool(Event::class.java)