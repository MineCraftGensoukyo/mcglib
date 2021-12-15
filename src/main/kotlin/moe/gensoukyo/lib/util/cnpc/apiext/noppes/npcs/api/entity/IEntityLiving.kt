@file:Suppress("Unused")

package moe.gensoukyo.lib.util.cnpc.apiext.noppes.npcs.api.entity

import moe.gensoukyo.lib.util.cnpc.ai.*
import net.minecraft.entity.EntityLivingBase
import noppes.npcs.api.entity.ICustomNpc
import noppes.npcs.api.entity.IEntityLivingBase
import java.util.*
import net.minecraft.entity.EntityLiving as McpType
import noppes.npcs.api.entity.IEntityLiving as ApiType

/**
 * Adds an AI task to minecraft's AI system.
 * Your change will disappear later if this entity is a CustomNPC.
 *
 * @see ICustomNpc.replaceMeleeAi for npcs.
 * @see ICustomNpc.replaceRangedAi for npcs.
 */
fun <T : McpType> ApiType<T>.addTask(priority: Int, task: NpcAiBase) {
    mcEntity.tasks.addTask(priority, task.mcAi)
}

val <T : McpType> ApiType<T>.aiManager: IEntityAiManager
    get() = this.mcEntity.tasks.npcApi

val <T : McpType> ApiType<T>.hostilityManager: IEntityAiManager
    get() = this.mcEntity.targetTasks.npcApi