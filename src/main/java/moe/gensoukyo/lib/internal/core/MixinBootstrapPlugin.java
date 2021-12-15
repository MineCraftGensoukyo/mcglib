package moe.gensoukyo.lib.internal.core;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.serverct.ersha.jd.F;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Objects;

/**
 * @author ChloePrime
 */
@Name("MCGLib MixinBootstrapPlugin")
public class MixinBootstrapPlugin implements IFMLLoadingPlugin {

    @SuppressWarnings("unused")
    public static void initMixin() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.mcglib.json");
    }

    /**
     * Adds any .jar in `mods` folder to LaunchClassLoader.
     */
    private static void fixJvmMagic(File myJar) {
        File[] mods = myJar.getParentFile().listFiles();
        if (mods == null) {
            throw new ExceptionInInitializerError("Cannot find mods folder!");
        }
        for (File modJar : mods) {
            if (!modJar.isFile() || !modJar.getName().endsWith(".jar")) {
                continue;
            }
            try {
                Launch.classLoader.addURL(modJar.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    @Override
    public void injectData(@Nonnull Map<String, Object> data) {
        fixJvmMagic((File) data.get("coremodLocation"));
        try {
            ClassLoader appClassLoader = Launch.class.getClassLoader();
            MethodUtils.invokeMethod(appClassLoader, true, "addURL", this.getClass().getProtectionDomain().getCodeSource().getLocation());
            MethodUtils.invokeStaticMethod(appClassLoader.loadClass(this.getClass().getName()), "initMixin");
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
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
