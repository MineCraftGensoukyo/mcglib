package moe.gensoukyo.lib.internal.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.ITransformerProvider;
import org.spongepowered.asm.service.MixinService;

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
        return excludeDelegate(new String[] {
                "moe.gensoukyo.lib.internal.core.NpcInterfaceTransformer"
        });
    }

    // 防止重入
    private static String[] excludeDelegate(String[] ary) {
        IMixinService service = MixinService.getService();
        ITransformerProvider itp = service == null ? null : service.getTransformerProvider();
        if (itp != null) {
            for (String str : ary) itp.addTransformerExclusion("$wrapper.".concat(str));
        }
        return ary;
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
