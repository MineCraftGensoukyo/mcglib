package moe.gensoukyo.lib.util.cnpc

import moe.gensoukyo.lib.maps.DataToken
import moe.gensoukyo.lib.reflection.get
import moe.gensoukyo.lib.reflection.set
import noppes.npcs.api.IWorld
import noppes.npcs.api.block.IBlock
import noppes.npcs.api.entity.IEntity
import noppes.npcs.api.entity.data.IData
import noppes.npcs.api.item.IItemStack
import java.util.function.Supplier
import kotlin.reflect.KProperty

/**
 * ## Example Usage
 *
 * ```kotlin
 * var IWorld.foo by tempdata<Unit>()
 * var IWorld.bar by tempdata { 0L }
 *
 * val world = NpcAPI.Instance().getIWorld(0)
 * assert(world.foo == null)
 * world.foo = Unit
 * world.foo = null
 * world.bar = 2L
 * ```
 */
@Suppress("UNUSED")
inline fun <reified T : Any> tempdata(
    propName: String? = null, crossinline initializer: () -> T? = { null }
) = IDataDelegateProvider(
    propName, T::class.javaObjectType, Supplier { initializer() },
    false
)

@Suppress("UNUSED")
inline fun <reified T : Any> storeddata(
    propName: String? = null, crossinline initializer: () -> T? = { null }
) = IDataDelegateProvider(
    propName, T::class.javaObjectType, Supplier { initializer() },
    true
)

class IDataDelegate<T>(
    private val dataToken: DataToken<T>,
    private val useStoreddata: Boolean
) {
    /**
     * IBlock / get
     */
    operator fun getValue(thisRef: IBlock, property: KProperty<*>): T {
        return choose(thisRef.tempdata, thisRef.storeddata)[dataToken]
    }

    /**
     * IBlock / set
     */
    operator fun setValue(thisRef: IBlock, property: KProperty<*>, value: T) {
        choose(thisRef.tempdata, thisRef.storeddata)[dataToken] = value
    }

    /**
     * IEntity / get
     */
    operator fun getValue(thisRef: IEntity<*>, property: KProperty<*>): T {
        return choose(thisRef.tempdata, thisRef.storeddata)[dataToken]
    }

    /**
     * IEntity / set
     */
    operator fun setValue(thisRef: IEntity<*>, property: KProperty<*>, value: T) {
        choose(thisRef.tempdata, thisRef.storeddata)[dataToken] = value
    }

    /**
     * IItemStack / get
     */
    operator fun getValue(thisRef: IItemStack, property: KProperty<*>): T {
        return choose(thisRef.tempdata, thisRef.storeddata)[dataToken]
    }

    /**
     * IItemStack / set
     */
    operator fun setValue(thisRef: IItemStack, property: KProperty<*>, value: T) {
        choose(thisRef.tempdata, thisRef.storeddata)[dataToken] = value
    }

    /**
     * IWorld / get
     */
    operator fun getValue(thisRef: IWorld, property: KProperty<*>): T {
        return choose(thisRef.tempdata, thisRef.storeddata)[dataToken]
    }

    /**
     * IWorld / set
     */
    operator fun setValue(thisRef: IWorld, property: KProperty<*>, value: T) {
        choose(thisRef.tempdata, thisRef.storeddata)[dataToken] = value
    }

    private fun choose(tempdata: IData, storeddata: IData) =
        if (useStoreddata) {
            storeddata
        } else {
            tempdata
        }

    operator fun getValue(thisRef: Map<String, Any>, property: KProperty<*>): T {
        return thisRef[dataToken]
    }

    operator fun setValue(thisRef: Map<String, Any>, property: KProperty<*>, value: T) {
        thisRef[dataToken] = value
    }
}

class IDataDelegateProvider<T>(
    private val propName: String?,
    private val typeOfT: Class<T>,
    private val initializer: Supplier<T?>,
    private val useStoreddata: Boolean
) {
    operator fun provideDelegate(
        ignored: Any?,
        property: KProperty<*>
    ) = IDataDelegate(
        DataToken(propName ?: property.name, typeOfT, initializer),
        useStoreddata
    )
}