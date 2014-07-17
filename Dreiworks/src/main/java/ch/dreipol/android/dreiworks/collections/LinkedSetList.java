package ch.dreipol.android.dreiworks.collections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import rx.functions.Func1;

/**
 * Created by melbic on 16/07/14.
 * Linked List which takes and key to detect if an object is already in th list
 */
public class LinkedSetList<K, O> implements Iterable<O> {
    private Set<K> mKeySet;
    private LinkedList<O> mObjects;
    private Func1<O, K> mSelectorFunction;

    public LinkedSetList(Func1<O, K> selectorFunc) {
        mSelectorFunction = selectorFunc;
        mKeySet = new HashSet<K>();
        mObjects = new LinkedList<O>();
    }

    public boolean add(O object) {
        K key = keyForObject(object);
        boolean contains = mKeySet.contains(key);
        if (!contains) {
            mKeySet.add(key);
            mObjects.add(object);
        }
        return !contains;
    }

    public boolean addAll(Collection<? extends O> collection) {
        boolean anythingAdded = false;
        for (O object : collection) {
            if (!contains(object)) {
                mObjects.addLast(object);
                anythingAdded = true;
            }
        }
        return anythingAdded;
    }

    public boolean addAll(Map<K, O> map) {
        map.keySet().removeAll(mKeySet);
        mObjects.addAll(map.values());
        return mKeySet.addAll(map.keySet());
    }

    public void clear() {
        mObjects.clear();
        mKeySet.clear();

    }

    public boolean contains(Object object) {
//        TODO: does it work like this?
        K key;
        try {
            key = keyForObject(object);
        } catch (ClassCastException c) {
            return false;
        }
        boolean contains = mKeySet.contains(key);
        return contains;
    }

    private K keyForObject(Object object) throws ClassCastException {
        O o = (O) object;
        return mSelectorFunction.call(o);
    }

    /**
     * Creates a getIterator set with n iterators
     * @param n
     * @return the CompoundIterator
     */
//    public Collection<java.util.Iterator<O>> iterators(int n) {
//
//    }

    @Override
    public Iterator<O> iterator() {
        return new Iterator<O>();
    }


    public int size() {
        return mObjects.size();
    }

    private void removeKeyForObject(Object object) {
        K k = keyForObject(object);
        mKeySet.remove(k);
    }

    class Iterator<T> implements FutureAwareIterator<O> {
        private int mIndex = 0;

        @Override
        public boolean hasNext() {
            return mIndex < mObjects.size();
        }

        @Override
        public O next() {
            return mObjects.get(mIndex++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int aheadCount() {
            return mObjects.size() - mIndex;
        }
    }
}
