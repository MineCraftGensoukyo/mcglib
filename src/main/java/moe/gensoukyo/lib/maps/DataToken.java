package moe.gensoukyo.lib.maps;

import com.google.common.base.Preconditions;
import moe.gensoukyo.lib.constants.ModIds;
import net.minecraftforge.fml.common.Optional;
import noppes.npcs.api.entity.data.IData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Retrieves data from typeless maps,
 * especially CNPC's Temp Data.
 *
 * Refill the value with the given supplier
 * if data in the map is null or is not the right type.
 *
 * @param <T> The type of data in a map of mess
 * @author ChloePrime
 */
@SuppressWarnings("unused")
public class DataToken<T> {
    @Nonnull
    private final String key;
    @Nonnull
    private final Class<T> type;
    @Nonnull
    private final Supplier<T> supplier;

    public DataToken(@Nonnull String key,
                     @Nonnull Class<T> type,
                     @Nonnull Supplier<T> supplier) {
        Preconditions.checkNotNull(key, "Data Key Must Not be Null");
        Preconditions.checkNotNull(key, "Data Type Must Not be Null");
        Preconditions.checkNotNull(supplier, "There must be a supplier to refill the value.");

        this.key = key;
        this.supplier = supplier;
        this.type = type;
    }

    @Nonnull
    public T get(Map<String, Object> map) {
        T result = peek(map);
        if (result != null) {
            return result;
        }
        T t = supplier.get();
        put(map, t);
        return t;
    }

    /**
     * Return the value in the map
     * if it has the correct class,
     * Else return null.
     * <p>
     * Never refills the value.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public T peek(Map<String, Object> map) {
        Object result = map.get(key);
        if (type.isInstance(result)) {
            return ((T) result);
        }
        return null;
    }

    @Nullable
    public T put(Map<String, Object> map, Object value) {
        T prev = peek(map);
        map.put(key, value);
        return prev;
    }

    /**
     * Resets the value to default (newly created object from the supplier)
     *
     * @return Value in the map before
     */
    @Nullable
    public T reset(Map<String, Object> map) {
        return put(map, supplier.get());
    }

    /**
     * Removes the value in the map.
     * Used to free memory.
     *
     * @return Object in the map before.
     */
    @Nullable
    public T clear(Map<String, Object> map) {
        T result = peek(map);
        map.remove(this.key);
        return result;
    }

    // CNPC Support

    @Optional.Method(modid = ModIds.CNPC)
    public T get(IData map) {
        return get(new CnpcDataWrapper(map));
    }

    @Nullable
    @Optional.Method(modid = ModIds.CNPC)
    public T peek(IData map) {
        return peek(new CnpcDataWrapper(map));
    }

    @Nullable
    @Optional.Method(modid = ModIds.CNPC)
    public T put(IData map, Object value) {
        return put(new CnpcDataWrapper(map), value);
    }

    @Nullable
    @Optional.Method(modid = ModIds.CNPC)
    public T reset(IData map) {
        return reset(new CnpcDataWrapper(map));
    }

    @Nullable
    @Optional.Method(modid = ModIds.CNPC)
    public T clear(IData map) {
        return clear(new CnpcDataWrapper(map));
    }
}
