@file:Suppress("unused")

package moe.gensoukyo.lib.util.cnpc.gameplay

import moe.gensoukyo.lib.timer.ITask
import noppes.npcs.api.entity.IEntity
import java.lang.ref.WeakReference

/**
 * example:
 *
 * ```kotlin
 * val task: ITask = SOME_CONSTANT_TASK_SCHEDULER
 * val npc: ICustomNpc<*> = getSomeNpc()
 * val npc: ICustomNpc<*> = getSomeNpc()
 *
 * val tracker = EntityTracker(npc)
 *     .onUpdate {
 *         it.say("x=${x},y=${y},z=${z}")
 *     }
 *     .onFinalize {
 *         // Create an explosion at the entity's last known location.
 *         explode(x, y, z)
 *     }
 * task.repeat(1, tracker::update)
 * ```
 *
 * @see ITask.repeat
 */
open class EntityTracker<E : IEntity<*>> private constructor(
    private val entity: WeakReference<E>,
    private var onUpdate: (EntityTracker<E>.(E) -> Unit)? = null,
    private var onFinalize: (EntityTracker<E>.(E?) -> Unit)? = null,
) {
    val x get() = entity.get()?.x ?: lastX
    val y get() = entity.get()?.y ?: lastY
    val z get() = entity.get()?.z ?: lastZ
    private var lastX = 0.0
    private var lastY = 0.0
    private var lastZ = 0.0

    constructor(entity: E): this(WeakReference(entity), null, null)

    fun onUpdate(consumer: EntityTracker<E>.(E) -> Unit): EntityTracker<E> {
        this.onUpdate = consumer
        return this
    }

    fun onFinalize(consumer: EntityTracker<E>.(E?) -> Unit): EntityTracker<E> {
        this.onFinalize = consumer
        return this
    }

    fun update(task: ITask.ITaskItem) {
        val e = entity.get()
        if (e == null || !e.isAlive) {
            task.stop()
            finalizeMe(e)
            return
        }
        lastX = e.x
        lastY = e.y
        lastZ = e.z
        onUpdate(e)
    }

    fun explode() {
        finalizeMe(entity.get())
        entity.clear()
    }

    protected open fun onUpdate(entity: E) {
        onUpdate?.invoke(this, entity)
    }

    protected open fun onFinalize(entity: E?) {
        onFinalize?.invoke(this, entity)
    }

    private fun finalizeMe(entity: E?) {
        try {
            onFinalize?.invoke(this, entity)
        } finally {
            onUpdate = null
            onFinalize = null
        }
    }
}