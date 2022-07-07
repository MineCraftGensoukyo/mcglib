package moe.gensoukyo.lib.internal.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import org.spongepowered.asm.launch.MixinBootstrap;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author ChloePrime
 */
@Name("MCGLib MixinBootstrapPlugin")
public class MixinBootstrapPlugin implements IFMLLoadingPlugin {

    @SuppressWarnings("unused")
    public static void initMixin() {
        MixinBootstrap.init();
    }

    static {
        initMixin();
    }

    @Override
    public void injectData(@Nonnull Map<String, Object> data) {
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                "moe.gensoukyo.lib.internal.core.NpcInterfaceTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
