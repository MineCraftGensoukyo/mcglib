@file:Suppress("unused")
@file:SideOnly(Side.SERVER)

package moe.gensoukyo.lib.server.ap

import moe.gensoukyo.lib.constants.ModIds
import moe.gensoukyo.lib.server.bukkit
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import noppes.npcs.api.entity.IEntity
import org.bukkit.entity.Entity
import org.serverct.ersha.jd.api.EntityAttributeAPI
import java.util.*

/**
 * Grants self extra AP attributes within the scope of [action]
 *
 * @param attributes Attribute list with amount, e.g. listOf("物理伤害:20")
 * @author ChloePrime
 */
inline fun Entity.withAttribute(
    attributes: List<String>,
    action: () -> Unit
) {
    val source = UUID.randomUUID().toString()
    EntityAttributeAPI.addEntityAttribute(this, source, attributes)
    try {
        action()
    } finally {
        EntityAttributeAPI.removeEntityAttribute(this, source)
    }
}

/**
 * Grants self extra AP attributes within the scope of [action]
 *
 * @param attribute Attribute list without amount
 * @param values Attribute amounts
 * @author ChloePrime
 */
inline fun Entity.withAttribute(
    attribute: Array<String>,
    values: DoubleArray,
    action: () -> Unit
) {
    require(attribute.size == values.size) {
        "Amount of attribute names and values should be the same"
    }

    val assembledAttributes = attribute.mapIndexed { index, attributeName ->
        "$attributeName:${values[index]}"
    }
    this.withAttribute(assembledAttributes, action)
}

/**
 * CNPC Variant of those functions
 *
 * @author ChloePrime
 */
@Optional.Method(modid = ModIds.CNPC)
inline fun IEntity<*>.withAttribute(
    attributes: List<String>,
    action: () -> Unit
) = this.bukkit.withAttribute(attributes, action)

/**
 * CNPC Variant of those functions
 *
 * @author ChloePrime
 */
@Optional.Method(modid = ModIds.CNPC)
inline fun IEntity<*>.withAttribute(
    attribute: Array<String>,
    values: DoubleArray,
    action: () -> Unit
) = this.bukkit.withAttribute(attribute, values, action)
