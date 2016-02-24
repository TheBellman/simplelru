package net.parttimepolymath.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleLRUCacheTest {

    private static SimpleLRUCache<String, String> instance;
    private static int cacheSize = 5;

    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = new SimpleLRUCache<String, String>(cacheSize);
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

    /**
     * Test of get method, of class SimpleLRUCache.
     */
    @Test
    public void testGet() {
        String key = "one";
        String expResult = "one";
        String result = instance.get(key);
        assertEquals(expResult, result);
    }

    /**
     * Test of put method, of class SimpleLRUCache.
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
     * Test of clear method, of class SimpleLRUCache.
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
     * Test of usedEntries method, of class SimpleLRUCache.
     */
    @Test
    public void testUsedEntries() {
        int expResult = cacheSize;
        int result = instance.usedEntries();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAll method, of class SimpleLRUCache.
     */
    @Test
    public void testGetAll() {
        @SuppressWarnings("rawtypes")
        Collection result = instance.getAll();
        assertNotNull(result);
        assertEquals(result.size(), cacheSize);
    }
}
