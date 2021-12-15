@file:Suppress("unused")

package moe.gensoukyo.lib.util.cnpc.ai

import com.google.common.annotations.Beta
import moe.gensoukyo.lib.internal.core.mixins.MixinNpcInterface
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.EntityAIBase
import noppes.npcs.entity.EntityNPCInterface
import java.util.function.Consumer

/**
 * @see MixinNpcInterface 实现类
 */
interface CnpcAiBuilder {
    /**
     * Unsafe,
     * Overrides [EntityNPCInterface.updateTasks]
     */
    @Beta
    fun overrideAllAiCompletely(builder: Consumer<CnpcAiBuilder>?)

    /**
     * Clear AI tasks
     * and replace with ourselves.
     */
    fun overrideAllAi(builder: Consumer<CnpcAiBuilder>) {
        overrideRegularAi(builder)
        overrideDoorInteractionAi(Consumer { })
        overrideSeekShelterAi(Consumer { })
        overrideBattleAi(Consumer { })
        overrideMoveAi(Consumer { })
    }

    fun resetOverrides()

    /**
     * @see EntityNPCInterface.addRegularEntries
     */
    fun overrideRegularAi(builder: Consumer<CnpcAiBuilder>?)

    /**
     * @see EntityNPCInterface.doorInteractType
     */
    fun overrideDoorInteractionAi(builder: Consumer<CnpcAiBuilder>?)

    /**
     * @see EntityNPCInterface.seekShelter
     */
    fun overrideSeekShelterAi(builder: Consumer<CnpcAiBuilder>?)

    /**
     * @see EntityNPCInterface.setResponse
     */
    fun overrideBattleAi(builder: Consumer<CnpcAiBuilder>?)

    /**
     * @see EntityNPCInterface.setMoveType
     */
    fun overrideMoveAi(builder: Consumer<CnpcAiBuilder>?)

    /**
     * [EntityNPCInterface.taskCount]++
     */
    val taskCounter: Int

    /**
     * @return The actual task priority inside [EntityLiving.tasks]
     */
    fun addTask(task: EntityAIBase): Int {
        return addTask(1, task)
    }

    /**
     * @param relativePriority relative priority against [EntityNPCInterface.taskCount]
     * @return The actual task priority inside [EntityLiving.tasks]
     */
    fun addTask(relativePriority: Int, task: EntityAIBase): Int
}

fun CnpcAiBuilder.addTask(task: NpcAiBase) =
    addTask(task.mcAi)

fun CnpcAiBuilder.addTask(relativePriority: Int, task: NpcAiBase) =
    addTask(relativePriority, task.mcAi)