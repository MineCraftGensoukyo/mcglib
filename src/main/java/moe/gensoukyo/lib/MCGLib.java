package moe.gensoukyo.lib;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
}
