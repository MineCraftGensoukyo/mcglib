package moe.gensoukyo.lib.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * A writer that writes things to an ItemStack.
 *
 * @author ChloePrime
 */
public class LoreWriter extends Writer {
    public static PrintWriter printer(ItemStack backend) {
        return new PrintWriter(new LoreWriter(backend));
    }

    public LoreWriter(ItemStack item) {
        this.item = item;
    }

    private final ItemStack item;
    private final NBTTagList stackTrace = new NBTTagList();

    public ItemStack getItem() {
        return item;
    }

    @Override
    public void write(@Nonnull char[] cbuf, int off, int len) {
        StringBuilder sb = newStringBuilder();

        for (int i = off; i < len; i++) {
            char c = cbuf[i];
            if (c == '\r' || c == '\n') {
                stackTrace.appendTag(new NBTTagString(sb.toString()));
                sb = newStringBuilder();
                continue;
            }
            sb.append(c);
        }
        String finalLine = sb.toString();
        if (!finalLine.isEmpty()) {
            stackTrace.appendTag(new NBTTagString(finalLine));
        }
    }

    private static StringBuilder newStringBuilder() {
        return new StringBuilder("§c");
    }

    @Override
    public void flush() {
        item.getOrCreateSubCompound("display").setTag("lore", stackTrace);
    }

    @Override
    public void close() {
        flush();
    }
}
