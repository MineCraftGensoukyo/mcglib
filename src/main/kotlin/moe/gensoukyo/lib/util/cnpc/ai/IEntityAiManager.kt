package moe.gensoukyo.lib.util.cnpc.ai

import net.minecraft.entity.ai.EntityAITasks
import java.util.stream.Stream

data class TaskEntry(
    val priority: Int,
    val task: NpcAiBase
)

/**
 * A wrapper of [EntityAITasks]
 * @see EntityAITasks
 */
interface IEntityAiManager {
    val entries: Stream<TaskEntry>
    fun addTask(priority: Int, task: NpcAiBase)
    fun remove(task: NpcAiBase)

    /**
     * @see EntityAITasks.isControlFlagDisabled
     */
    fun isMutexLocked(mutexBits: Int): Boolean

    /**
     * @see EntityAITasks.disableControlFlag
     */
    fun acquireLock(mutexBits: Int)

    /**
     * @see EntityAITasks.enableControlFlag
     */
    fun releaseLock(mutexBits: Int)

    class Impl(val mcAiManager: EntityAITasks) : IEntityAiManager {

        override val entries: Stream<TaskEntry>
            get() = mcAiManager.taskEntries.stream().map {
                TaskEntry(it.priority, it.action.npcApi)
            }

        override fun addTask(priority: Int, task: NpcAiBase) {
            mcAiManager.addTask(priority, task.mcAi)
        }

        override fun remove(task: NpcAiBase) {
            mcAiManager.removeTask(task.mcAi)
        }

        override fun isMutexLocked(mutexBits: Int) = mcAiManager.isControlFlagDisabled(mutexBits)
        override fun acquireLock(mutexBits: Int) = mcAiManager.disableControlFlag(mutexBits)
        override fun releaseLock(mutexBits: Int) = mcAiManager.enableControlFlag(mutexBits)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Impl

            if (mcAiManager != other.mcAiManager) return false

            return true
        }

        override fun hashCode(): Int {
            return mcAiManager.hashCode()
        }
    }
}

val EntityAITasks.npcApi: IEntityAiManager get() = IEntityAiManager.Impl(this)
