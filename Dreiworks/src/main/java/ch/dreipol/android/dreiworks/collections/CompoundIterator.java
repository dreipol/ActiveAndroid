package ch.dreipol.android.dreiworks.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by melbic on 17/07/14.
 */
public class CompoundIterator<T> {


    private final ArrayList<ILazyIterator<T>> mIterators;
    private int mIteratorCount;
    private List<T> mOriginCollection;
    private AtomicInteger mNextIndex;

    public CompoundIterator(int n, List<T> originCollection) {
        mIteratorCount = n;
        mOriginCollection = originCollection;
        mIterators = new ArrayList<ILazyIterator<T>>(n);
        mNextIndex = new AtomicInteger(0);
        createIterators(n);
    }

    private void createIterators(int n) {
        for (int i = 0; i < n; i++) {
            mIterators.add(i, new LazyIterator<T>());
        }
    }

    public ILazyIterator<T> getIterator(int i) throws NoSuchElementException {
        if (i > mIteratorCount) {
            throw new NoSuchElementException();
        } else {
            return mIterators.get(i);
        }
    }

    //TODO: Atomic und so...
    private int nextIndex() {
//        int current = mNextIndex.get();
//        int next = current + 1;
//        while (!mNextIndex.compareAndSet(current, next) && next < mOriginCollection.size()) {
//            next++;
//        }
//        if (current >= mOriginCollection.size()) {
//            throw new IndexOutOfBoundsException();
//        }
//        return current;
        int next = mNextIndex.getAndIncrement();
        if(next < mOriginCollection.size()) {
        return next;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    private boolean _hasNext() {
        return mNextIndex.get() < mOriginCollection.size();
    }

    class LazyIterator<U> implements ILazyIterator<T> {


        private int mIndex = -1;

        @Override
        public T head() {
            if (mIndex == -1) {
                throw new IllegalAccessError("The Iterator has to be moved before accessing the head.");
            }
            return mOriginCollection.get(mIndex);
        }

        @Override
        public boolean hasNext() {
            return _hasNext();
        }

        @Override
        public void move() throws NoSuchElementException {
            mIndex = nextIndex();
        }
    }
}
