package ch.dreipol.android.dreiworks.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.functions.Func1;

//TODO: using map implement Map<K,O>
/**
 * Created by melbic on 16/07/14.
 * Linked List which takes and key to detect if an object is already in th list
 */
public class FunctionMap<K, O> implements Iterable<O> {
    private Set<K> mKeySet;
    private List<O> mObjects;
    private Func1<O, K> mSelectorFunction;
    private Iterator<O> mIterator;

    public FunctionMap(Func1<O, K> selectorFunc) {
        mSelectorFunction = selectorFunc;
        mKeySet = new HashSet<K>();
        mObjects = new ArrayList<O>();
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
                mObjects.add(object);
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

    public Set<K> keySet() {
        return mKeySet;
    }

    public List<O> values() {
        return mObjects;
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

    public void setIterator(Iterator<O> iterator) {

        mIterator = iterator;
    }

    @Override
    public Iterator<O> iterator() {

        return _iterator();
    }

    private Iterator<O> _iterator() {
        if (mIterator != null) {
            return mIterator;
        } else {
            return mObjects.iterator();
        }
    }


    public int size() {
        return mObjects.size();
    }

    private void removeKeyForObject(Object object) {
        K k = keyForObject(object);
        mKeySet.remove(k);
    }
}
