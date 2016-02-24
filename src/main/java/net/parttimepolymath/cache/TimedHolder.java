package net.parttimepolymath.cache;

import java.util.concurrent.atomic.AtomicReference;

import net.jcip.annotations.ThreadSafe;

/**
 * this class will hold something for a period of time. If the time limit is expired, so are the contents.
 * This class is thread safe in that multiple threads accessing it will not break it, however if one thread stores something,
 * and a second also stores something, the first thread of course will not see what it stored. I do not recommend accessing
 * a single instance of this from multiple threads unless you are happy with that behaviour.
 * 
 * @param <T> the type of thing held.
 * @author R. Hook
 */
@ThreadSafe
public class TimedHolder<T> {

    /**
     * container for the contents. of course.
     */
    private final AtomicReference<T> holder = new AtomicReference<>();
    /**
     * the TTL for items before they expire.
     */
    private final int ttl;
    /**
     * the time at which the current item will expire.
     */
    private long expiry;

    /**
     * primary constructor.
     * 
     * @param timeToLive the time to live in milliseconds for items held by this holder.
     */
    public TimedHolder(final int timeToLive) {
        ttl = timeToLive;
    }

    /**
     * alternate constructor.
     * 
     * @param timeToLive the time to live in milliseconds for items held by this holder.
     * @param content the content to store. Silently ignored if null.
     */
    public TimedHolder(final int timeToLive, final T content) {
        ttl = timeToLive;
        setContent(content);
    }

    /**
     * store content in the holder, which will also reset the expire time.
     * 
     * @param content the content to store. Silently ignored if null.
     */
    public final void setContent(final T content) {
        if (content != null) {
            holder.set(content);
            expiry = System.currentTimeMillis() + ttl;
        }
    }

    /**
     * retrieve the content.
     * 
     * @return the content of the holder, or null if the content has expired.
     */
    public final T getContent() {
        if (System.currentTimeMillis() > expiry) {
            holder.set(null);
        }
        return holder.get();
    }
}
