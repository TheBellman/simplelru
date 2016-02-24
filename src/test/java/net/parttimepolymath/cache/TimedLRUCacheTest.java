package net.parttimepolymath.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimedLRUCacheTest {

    private static TimedLRUCache<String, String> instance;
    private static int cacheSize = 5;

    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = new TimedLRUCache<String, String>(cacheSize, 100);
    }

    @Before
    public void setUp() {
        instance.put("one", "one");
        instance.put("two", "two");
        instance.put("three", "three");
        instance.put("four", "four");
        instance.put("five", "five");
    }

    @After
    public void tearDown() {
        instance.clear();
    }

    @Test
    public void testGetExpiry() {
        instance.put("expire", "this should vanish");
        pause(50);
        assertEquals("this should vanish", instance.get("expire"));
        pause(200);
        assertNull(instance.get("expire"));
    }

    @Test
    public void testTouchAndGet() {
        instance.put("noexpire", "this should remain");
        pause(50);
        assertEquals("this should remain", instance.touchAndGet("noexpire"));
        pause(75);
        // 125 ms after we stored it, this will have expired if we had not touched it.
        assertNotNull(instance.get("noexpire"));
        // 125 ms after we touched it, it should have expired.
        pause(50);
        assertNull(instance.get("noexpire"));
    }

    /**
     * Test of get method, of class TimedLRUCache.
     */
    @Test
    public void testGet() {
        String key = "one";
        String expResult = "one";
        String result = instance.get(key);
        assertEquals(expResult, result);
    }

    @Test
    public void testRemove() {
        instance.put("kill me", "or bring me liberty");
        assertNotNull(instance.get("kill me"));
        instance.remove("kill me");
        assertNull(instance.get("kill me"));
    }

    /**
     * Test of put method, of class TimedLRUCache.
     */
    @Test
    public void testPut() {
        String key = "six";
        String value = "six";

        instance.put(key, value);

        String result = instance.get(key);
        assertEquals(value, result);
    }

    @Test
    public void testEjection() {
        String key = "six";
        String value = "six";

        instance.put(key, value);
        String result = instance.get(key);
        assertEquals(value, result);

        assertNull(instance.get("one"));
        assertNotNull(instance.get("two"));
        assertNotNull(instance.get("three"));
        assertNotNull(instance.get("four"));
        assertNotNull(instance.get("five"));
        assertNotNull(instance.get("six"));
    }

    /**
     * Test of clear method, of class TimedLRUCache.
     */
    @Test
    public void testClear() {
        instance.clear();
        assertNull(instance.get("one"));
        assertNull(instance.get("two"));
        assertNull(instance.get("three"));
        assertNull(instance.get("four"));
        assertNull(instance.get("five"));
        assertNull(instance.get("six"));
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey("three"));
        assertFalse(instance.containsKey("a small fish"));
    }

    /**
     * Test of usedEntries method, of class TimedLRUCache.
     */
    @Test
    public void testUsedEntries() {
        int expResult = cacheSize;
        int result = instance.usedEntries();
        assertEquals(expResult, result);
    }

    private static void pause(final int millis) {
        if (millis > 0) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
