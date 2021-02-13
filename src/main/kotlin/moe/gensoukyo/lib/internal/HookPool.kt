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
object HookPool: ClassValue<Array<EnumScriptType>>() {
    private val upperBound = Object::class.java

    override fun computeValue(type: Class<*>): Array<EnumScriptType> {
        val myEnum = getEnum(type) ?: return arrayOf()
        val superman = type.superclass
        if (superman == null || !superman.isValidClass()) {
            return arrayOf()
        }
        val result = mutableListOf(myEnum)
        result.addAll(get(superman))
        return result.toTypedArray()
    }

    /**
     * Valid classes should be a child of [Object],
     * not [Object] itself.
     */
    private fun Class<*>.isValidClass(): Boolean {
        return this != upperBound && upperBound.isAssignableFrom(this)
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
