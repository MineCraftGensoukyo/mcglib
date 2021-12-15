package moe.gensoukyo.lib.util.cnpc.ai

import net.minecraft.entity.ai.EntityAITasks
import java.util.stream.Stream

class TaskEntry(
    val priority: Int,
    val task: NpcAiBase
)

/**
 * @see EntityAITasks
 */
interface IEntityAiManager {
    val entries: Stream<TaskEntry>
    fun addTask(priority: Int, task: NpcAiBase)
    fun remove(task: NpcAiBase)

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
    }
}

val EntityAITasks.npcApi: IEntityAiManager get() = IEntityAiManager.Impl(this)
