package moe.gensoukyo.lib.internal.core.mixins;

import moe.gensoukyo.lib.internal.common.util.EqualsWrapper;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ModCandidate;
import net.minecraftforge.fml.common.discovery.ModDiscoverer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manually distinct repetitive ModCandidates.
 * @author ChloePrime
 */
@Mixin(value = ModDiscoverer.class, remap = false)
public class MixinModDiscover {
    @Shadow private List<ModCandidate> candidates;

    @Inject(
            method = "identifyMods",
            at = @At("HEAD")
    )
    private void injectIdentifyMods(CallbackInfoReturnable<List<ModContainer>> cir) {
        FMLLog.log.debug("Distinguishing mod containers.");
        candidates = candidates.stream()
                .map(EqualsWrapper::new)
                .distinct()
                .map(w -> w.wrapped)
                .collect(Collectors.toList());
    }
}

