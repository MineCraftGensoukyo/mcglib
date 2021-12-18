package moe.gensoukyo.lib;

import moe.gensoukyo.lib.internal.common.command.ModCommandsKt;
import moe.gensoukyo.lib.internal.common.network.MCGLibNetworkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * @author ChloePrime
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Mod(
        modid = MCGLib.MODID,
        name = MCGLib.NAME,
        version = MCGLib.VERSION,
        useMetadata = true
)
public class MCGLib
{
    public static final String MODID = "mcglib";
    public static final String NAME = "MCG Lib";
    public static final String VERSION = "1.12.0";

    public static Logger getLogger() {
        return logger;
    }

    private static Logger logger;

    /**
     * @return the config dir, will always ba an existing folder.
     */
    public static File getConfigDir() {
        return configDir;
    }

    private static File configDir;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configDir = new File(
                event.getModConfigurationDirectory(),
                MCGLib.MODID
        );
        try {
            FileUtils.forceMkdir(configDir);
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModConfig.reload();
        ModCommandsKt.initPermissions();
        MCGLibNetworkManager.init();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        ModCommandsKt.registerCommands(event);
    }
}
