package moe.gensoukyo.lib.util.cnpc.ai

import moe.gensoukyo.lib.internal.core.mixins.MixinNpcInterface
import net.minecraft.entity.ai.EntityAIBase
import java.util.function.Supplier

/**
 * @see MixinNpcInterface 实现类
 */
interface CnpcAiAccessor {
    /**
     * Replaces CNPC's melee AI,
     * Persists over AI resets.
     */
    fun replaceMeleeAiTask(taskConstructor: Supplier<EntityAIBase>?)

    /**
     * Replaces CNPC's ranged AI,
     * Persists over AI resets.
     */
    fun replaceRangedAiTask(taskConstructor: Supplier<EntityAIBase>?)

    fun resetMeleeAiTask() = replaceMeleeAiTask(null)
    fun resetRangedAiTask() = replaceRangedAiTask(null)
}