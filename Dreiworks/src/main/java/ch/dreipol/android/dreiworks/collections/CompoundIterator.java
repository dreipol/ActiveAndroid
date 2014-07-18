package ch.dreipol.android.dreiworks.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by melbic on 17/07/14.
 */
public class CompoundIterator<T> implements FutureAwareIterator<T> {


    private final ArrayList<ILazyIterator<T>> mIterators;
    private List<T> mOriginCollection;
    private AtomicInteger mNextIndex;

    public CompoundIterator(int n, List<T> originCollection) {
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
        if (i > mIterators.size()) {
            throw new NoSuchElementException();
        } else {
            return mIterators.get(i);
        }
    }

    public List<ILazyIterator<T>> iterators() {
        return mIterators;
    }

    //TODO: Atomic und so...

    /**
     * @param shouldTry returns -1 instead of throwing exception if no next element
     * @return
     */
    private int nextIndex(boolean shouldTry) {
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
        if (next < mOriginCollection.size()) {
            return next;
        } else if (shouldTry) {
            return -1;
        } else {
            throw new NoSuchElementException();
        }
    }

    private boolean _hasNext() {
        return mNextIndex.get() < mOriginCollection.size();
    }

    @Override
    public boolean hasNext() {
        return _hasNext();
    }

    @Override
    public T next() {
        return mOriginCollection.get(nextIndex(false));
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int aheadCount() {
        return mOriginCollection.size() - mNextIndex.get();
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
            mIndex = nextIndex(false);
        }

        @Override
        public boolean tryMove() {
            int nextIndex = hasNext() ? nextIndex(true) : -1;
            if (nextIndex != -1) {
                mIndex = nextIndex;
            }
            return (nextIndex != -1);
        }

        @Override
        public boolean headIsSet() {
            return mIndex != -1;
        }
    }
}
