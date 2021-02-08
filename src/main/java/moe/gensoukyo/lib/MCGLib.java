package moe.gensoukyo.lib;

import moe.gensoukyo.lib.internal.command.ModCommandsKt;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

/**
 * @author ChloePrime
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Mod(
        modid = MCGLib.MODID,
        name = MCGLib.NAME,
        version = MCGLib.VERSION,
        useMetadata = true,
        acceptableRemoteVersions = "*"
)
public class MCGLib
{
    public static final String MODID = "mcglib";
    public static final String NAME = "MCG Lib";
    public static final String VERSION = "@VERSION@";

    public static Logger getLogger() {
        return logger;
    }

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModCommandsKt.initPermissions();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        ModCommandsKt.registerCommands(event);
    }
}
