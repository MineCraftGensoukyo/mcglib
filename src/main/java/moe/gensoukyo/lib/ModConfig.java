package moe.gensoukyo.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import moe.gensoukyo.lib.internal.cnpc.ForgeEventWhitelist;

import java.io.*;

/**
 * @author ChloePrime
 */
public class ModConfig {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static String[] forgeEventWhitelist = {"java.lang.Fish"};

    public static void reload() {
        loadFromFile();
        compile();
    }

    private static void loadFromFile() {
        File cfgFile = new File(MCGLib.getConfigDir(), "event_whitelist.json");
        if (!cfgFile.exists()) {
            try (Writer writer = new BufferedWriter(new FileWriter(cfgFile))) {
                GSON.toJson(forgeEventWhitelist, writer);
            } catch (IOException exception) {
                MCGLib.getLogger().error(
                        "Failed to write default config",
                        exception
                );
            }
            return;
        }
        try (Reader reader = new BufferedReader(new FileReader(cfgFile))) {
            forgeEventWhitelist = GSON.fromJson(reader, String[].class);
        } catch (Exception exception) {
            MCGLib.getLogger().error(
                    "Failed to read from config",
                    exception
            );
        }
    }

    /**
     * Compiles data from config to internal data structure.
     */
    public static void compile() {
        ForgeEventWhitelist.acceptConfigData(forgeEventWhitelist);
    }
}