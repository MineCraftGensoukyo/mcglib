package moe.gensoukyo.lib.maps;

import noppes.npcs.api.entity.data.IData;

import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.Set;

/**
 * @author ChloePrime
 */
class CnpcDataWrapper
        extends AbstractMap<String, Object> {

    private final IData host;

    public CnpcDataWrapper(IData host) {
        this.host = host;
    }

    @Override
    public Object get(Object key) {
        String realKey = key.toString();
        return host.get(realKey);
    }

    @Override
    public Object put(String key, Object value) {
        Object prev = host.get(key);
        host.put(key, value);
        return prev;
    }

    @Override
    public Object remove(Object key) {
        host.remove(key.toString());
        return null;
    }

    @Nonnull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
