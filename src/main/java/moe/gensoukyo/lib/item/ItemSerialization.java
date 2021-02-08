package moe.gensoukyo.lib.item;

import com.google.common.annotations.Beta;
import com.google.gson.JsonSyntaxException;
import moe.gensoukyo.lib.MCGLib;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 道具序列化/反序列化到文件
 * 使用 MoJson
 *
 * @author ChloePrime
 */
@Beta
public class ItemSerialization {
    private static final Map<String, ItemStack> CACHE = new HashMap<>();
    private static final ItemStack ERROR = new ItemStack(
            Item.getItemFromBlock(Blocks.BARRIER)
    );

    static {
        ERROR.setStackDisplayName("§4Error Item");
    }

    public static ItemStack getUnsafely(String path)
            throws IOException {
        if (CACHE.containsKey(path)) {
            return CACHE.get(path);
        }
        return getFromFile(path);
    }

    /**
     * @return The item, when errored, returns a barrier with stacktrace on its lore.
     */
    @SuppressWarnings("unused")
    public static ItemStack get(String path) {
        ItemStack item;
        try {
            item = getUnsafely(path);
            CACHE.put(path, item);
        } catch (Exception e) {
            MCGLib.getLogger().error("Error deserializing item " + path, e);
            item = ERROR.copy();
            e.printStackTrace(LoreWriter.printer(item));
        }
        return item;
    }

    private static ItemStack getFromFile(String path) throws IOException {
        Path filePath = getFilePath(path);
        if (!filePath.toFile().exists()) {
            return new ItemStack(Items.AIR);
        }

        try {
            return new ItemStack(JsonToNBT.getTagFromJson(
                    String.join("\n", Files.readAllLines(filePath)))
            );
        } catch (NBTException e) {
            throw new JsonSyntaxException(e);
        }
    }

    @SuppressWarnings("unused")
    public static void put(String path, ItemStack itemStack)
            throws IOException {
        CACHE.put(path, itemStack);

        Path filePath = getFilePath(path);
        try (Writer writer = Files.newBufferedWriter(filePath)) {
            writer.write(itemStack.serializeNBT().toString());
        }
    }

    private static Path getFilePath(String path) throws IOException {
        String filePath = path.replace('.', '/') + ".json";
        Path result = Paths.get("./" + filePath);
        // Make sure parent path exists
        FileUtils.forceMkdirParent(result.toFile());
        return result;
    }


    /**
     * Clear item cache
     */
    public static void reload() {
        CACHE.clear();
    }

    private ItemSerialization() {}
}
