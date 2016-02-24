package net.parttimepolymath.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.jcip.annotations.ThreadSafe;

/**
 * a simple LRU cache based on top of the LinkedHashMap, capped at a certain size, with eject-oldest semantics.
 * 
 * @author R. Hook
 * @param <V> the value type of the cache
 * @param <K> the key type of the cache.
 */
@ThreadSafe
public final class SimpleLRUCache<K, V> {

    /**
     * hash factor.
     */
    private static final float LOADFACTOR = 0.75f;
    /**
     * wrapped map that actually contains the data.
     */
    private final Map<K, V> map;
    /**
     * maximum size of the cache.
     */
    private final int cacheSize;

    /**
     * Creates a new LRU cache.
     * 
     * @param size the maximum number of entries that will be kept in this cache.
     */
    public SimpleLRUCache(final int size) {
        cacheSize = size;
        int hashTableCapacity = (int) Math.ceil(size / LOADFACTOR) + 1;
        Map<K, V> aMap = new LinkedHashMap<K, V>(hashTableCapacity, LOADFACTOR, true) {
            private static final long serialVersionUID = 2674509550119308224L;

            @Override
            protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
                return size() > SimpleLRUCache.this.cacheSize;
            }
        };
        map = Collections.synchronizedMap(aMap);
    }

    /**
     * Retrieves an entry from the cache.<br>
     * The retrieved entry becomes the MRU (most recently used) entry.
     * 
     * @param key the key whose associated value is to be returned.
     * @return the value associated to this key, or null if no value with this key exists in the cache.
     */
    public V get(final K key) {
        return map.get(key);
    }

    /**
     * Adds an entry to this cache. The new entry becomes the MRU (most recently used) entry. If an entry with the
     * specified key already exists in the cache, it is replaced by the new entry. If the cache is full, the LRU (least
     * recently used) entry is removed from the cache.
     * 
     * @param key the key with which the specified value is to be associated.
     * @param value a value to be associated with the specified key.
     */
    public void put(final K key, final V value) {
        map.put(key, value);
    }

    /**
     * Clears the cache.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Returns the number of used entries in the cache.
     * 
     * @return the number of entries currently in the cache.
     */
    public int usedEntries() {
        return map.size();
    }

    /**
     * Returns a <code>Collection</code> that contains a copy of all cache entries.
     * 
     * @return a <code>Collection</code> with a copy of the cache content.
     */
    public Collection<Map.Entry<K, V>> getAll() {
        return new ArrayList<Map.Entry<K, V>>(map.entrySet());
    }

    /**
     * does the cache contain the supplied key?
     * 
     * @param key the key to test for
     * @return true if the key is present.
     */
    public boolean containsKey(final K key) {
        return map.containsKey(key);
    }

    /**
     * remove an item from the cache. This is primarily intended for use by TimedLRUCache and is of limited
     * value elsewhere.
     * 
     * @param key the key to clear.
     */
    public void clearItem(final K key) {
        map.remove(key);
    }
}
