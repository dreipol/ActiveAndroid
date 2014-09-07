package ch.dreipol.android.dreiworks.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by melbic on 17/07/14.
 * With a lazy getIterator you can get the current head several times without moving the getIterator.
 */
public interface ILazyIterator<T> {

    /**
     * Should throw IlegalStateException if the head get accessed before move it the first time
     * @return the current head. (without moving the getIterator)
     */
    public T head() throws IllegalStateException;

    public boolean hasNext();

    /**
     * Moves the head.
     *
     * @throws NoSuchElementException
     */
    public void move() throws NoSuchElementException;

    public boolean tryMove();
    public boolean headIsSet();
}
