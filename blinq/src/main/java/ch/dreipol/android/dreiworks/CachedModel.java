package ch.dreipol.android.dreiworks;

import java.util.Observable;

/**
 * Created by melbic on 26/08/14.
 */
public class CachedModel<T> extends Observable {

    private T mCachedObject;

    public CachedModel() {
        super();
    }

    public CachedModel(T cachedObject) {
        this();
        mCachedObject = cachedObject;
    }

    public void setCachedObject(T cachedObject) {
        mCachedObject = cachedObject;
        hasChanged();
        notifyObservers(mCachedObject);
    }

    public T getObject() {
        return mCachedObject;
    }
}
