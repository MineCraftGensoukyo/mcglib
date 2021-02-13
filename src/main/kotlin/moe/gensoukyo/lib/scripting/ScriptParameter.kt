package moe.gensoukyo.lib.scripting

import net.minecraftforge.fml.common.eventhandler.Event

/**
 * Parameter for passing non-event parameter to CNPC scripts
 */
class ScriptParameter<T>(
    @Suppress("unused") val payload: T
): Event()