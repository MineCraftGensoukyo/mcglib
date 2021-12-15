package moe.gensoukyo.lib.util.cnpc.ai;

/**
 * @author ChloePrime
 */
public interface NavOverrideable {
    /**
     * used by {@link CustomNpcRangedAiWrapper}
     * @param navOverride see {@link noppes.npcs.ai.EntityAIRangedAttack#navOverride(boolean)}
     */
    void navOverride(boolean navOverride);
}
