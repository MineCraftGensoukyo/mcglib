package moe.gensoukyo.lib.maps;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantConditions")
class DataTokenTest {

    private final Map<String, Object> testMap = new HashMap<>(1);

    private static final DataToken<AtomicInteger> token = new DataToken<>(
            "test",
            AtomicInteger.class,
            () -> new AtomicInteger(42)
    );

    @Test
    public void testBasic() {
        assertNull(token.peek(testMap), "New token should not refill value");

        AtomicInteger contained = token.get(testMap);
        assertEquals(42, contained.get());
        assertEquals(contained, testMap.get("test"));
        assertEquals(contained, token.get(testMap));

        contained.addAndGet(26);

        assertEquals(68, token.get(testMap).get());

        AtomicInteger updated = new AtomicInteger(996);
        assertEquals(68, token.put(testMap, updated).get());
        assertEquals(996, token.get(testMap).get());

        assertEquals(996, token.reset(testMap).get());
        assertEquals(42, ((AtomicInteger) testMap.get("test")).get());
        assertNotEquals(contained, token.get(testMap));
        assertNotEquals(updated, token.get(testMap));

        assertEquals(1, testMap.size());
    }
}