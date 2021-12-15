@file:Suppress("unused")

package moe.gensoukyo.lib.util.cnpc.ai

import com.google.common.annotations.Beta
import net.minecraft.entity.ai.EntityAIBase
import noppes.npcs.api.entity.ICustomNpc
import noppes.npcs.entity.EntityNPCInterface
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * Replace npc's melee attack AI.
 * Changes will stay across AI resets.
 */
inline fun ICustomNpc<*>.replaceMeleeAi(crossinline new: () -> NpcAiBase) {
    (mcEntity as CnpcAiAccessor).replaceMeleeAiTask(Supplier { new().mcAi })
}

/**
 * Replace npc's ranged attack AI.
 * Changes will stay across AI resets.
 */
inline fun ICustomNpc<*>.replaceRangedAi(crossinline new: () -> NpcAiBase) {
    (mcEntity as CnpcAiAccessor).replaceRangedAiTask(Supplier { new().mcAi })
}

/**
 * @see EntityNPCInterface.addRegularEntries
 */
inline fun ICustomNpc<*>.overrideRegularAi(crossinline builder: CnpcAiBuilder.() -> Unit) {
    (mcEntity as CnpcAiBuilder).overrideRegularAi(Consumer { builder(it) })
}

/**
 * @see EntityNPCInterface.doorInteractType
 */
inline fun ICustomNpc<*>.overrideDoorInteractionAi(crossinline builder: CnpcAiBuilder.() -> Unit) {
    (mcEntity as CnpcAiBuilder).overrideDoorInteractionAi(Consumer { builder(it) })
}

/**
 * @see EntityNPCInterface.seekShelter
 */
inline fun ICustomNpc<*>.overrideSeekShelterAi(crossinline builder: CnpcAiBuilder.() -> Unit) {
    (mcEntity as CnpcAiBuilder).overrideSeekShelterAi(Consumer { builder(it) })
}

/**
 * @see EntityNPCInterface.setResponse
 */
inline fun ICustomNpc<*>.overrideBattleAi(crossinline builder: CnpcAiBuilder.() -> Unit) {
    (mcEntity as CnpcAiBuilder).overrideBattleAi(Consumer { builder(it) })
}

/**
 * @see EntityNPCInterface.setMoveType
 */
inline fun ICustomNpc<*>.overrideMoveAi(crossinline builder: CnpcAiBuilder.() -> Unit) {
    (mcEntity as CnpcAiBuilder).overrideMoveAi(Consumer { builder(it) })
}

/**
 * Unsafe.
 * @see EntityNPCInterface.updateTasks
 */
@Beta
inline fun ICustomNpc<*>.overrideAllAiCompletely(crossinline builder: CnpcAiBuilder.() -> Unit) {
    (mcEntity as CnpcAiBuilder).overrideAllAiCompletely(Consumer { builder(it) })
}

val EntityAIBase.npcApi: NpcAiBase get() = NpcAiByMcp.of(this)