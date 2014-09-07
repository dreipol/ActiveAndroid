package ch.dreipol.android.dreiworks.collections;

import java.util.Iterator;

/**
 * Created by melbic on 17/07/14.
 */
public interface FutureAwareIterator<T> extends Iterator<T> {
    /**
     *
     * @returns the count of elements ahead of the current head.
     */
    public int aheadCount();
}
