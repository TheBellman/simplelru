package net.parttimepolymath.cache;

import net.jcip.annotations.ThreadSafe;

/**
 * a wrapper for the SimpleLRUCache where cached items have a maximum common TTL.
 * 
 * @author R. Hook
 * @param <V> the value type of the cache
 * @param <K> the key type of the cache.
 */
@ThreadSafe
public final class TimedLRUCache<K, V> {
    /**
     * the wrapped cache.
     */
    private final SimpleLRUCache<K, TimedHolder<V>> cache;
    /**
     * the time to live in milliseconds of cached items.
     */
    private final int ttl;

    /**
     * Creates a new LRU cache.
     * 
     * @param size the maximum number of entries that will be kept in this cache.
     * @param timeToLive the maximum time in milliseconds that an entry will survive.
     */
    public TimedLRUCache(final int size, final int timeToLive) {
        cache = new SimpleLRUCache<K, TimedHolder<V>>(size);
        ttl = timeToLive;
    }

    /**
     * Retrieves an entry from the cache. The retrieved entry becomes the MRU (most recently used) entry.
     * 
     * @param key the key whose associated value is to be returned.
     * @return the value associated to this key, or null if no value with this key exists in the cache.
     */
    public V get(final K key) {
        TimedHolder<V> holder = cache.get(key);
        if (holder != null) {
            synchronized (holder) {
                if (holder.getContent() == null) {
                    cache.clearItem(key);
                    return null;
                } else {
                    return holder.getContent();
                }
            }
        }
        return null;
    }

    /**
     * retrieves an entry and resets it's time to live. The retrieved entry becomes the MRU (most recently used) entry.
     * 
     * @param key the key whose associated value is to be returned.
     * @return The value, if available, otherwise null
     */
    public V touchAndGet(final K key) {
        TimedHolder<V> holder = cache.get(key);
        if (holder != null) {
            synchronized (holder) {
                V content = holder.getContent();
                if (content != null) {
                    holder.setContent(content);
                    return content;
                } else {
                    cache.clearItem(key);
                    return null;
                }
            }
        }

        return null;
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
        cache.put(key, new TimedHolder<V>(ttl, value));
    }

    /**
     * Clears the cache.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Returns the number of used entries in the cache.
     * 
     * @return the number of entries currently in the cache.
     */
    public int usedEntries() {
        return cache.usedEntries();
    }

    /**
     * does the cache contain the supplied key?
     * 
     * @param key the key to test for
     * @return true if the key is present.
     */
    public boolean containsKey(final K key) {
        return cache.containsKey(key);
    }

    /**
     * remove an item from the cache.
     * 
     * @param key the key to test for
     */
    public void remove(final K key) {
        cache.clearItem(key);

    }
}
