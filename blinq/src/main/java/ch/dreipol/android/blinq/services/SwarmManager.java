package ch.dreipol.android.blinq.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.dreiworks.collections.CompoundIterator;
import ch.dreipol.android.dreiworks.collections.FunctionMap;
import ch.dreipol.android.dreiworks.collections.ILazyIterator;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by melbic on 10/07/14.
 * <p/>
 * To use the swarm manager use one or both of the swarm iterators.
 * There are two iterators to use it like a ping-pong buffer.
 */
public class SwarmManager {
    public static final int ITERATOR_COUNT = 2;
    public static final int AHEAD_LOADING = 4;
    private final INetworkMethods mNetworkService;
    private final FunctionMap<Long, Profile> mProfilesMap;
    private final CompoundIterator<Profile> mProfileIterator;
    private SwarmObservable mProfilesChangedObservable;

    public SwarmManager() {
        mNetworkService = AppService.getInstance().getNetworkService();
        mProfilesMap = new FunctionMap<Long, Profile>(new Func1<Profile, Long>() {
            @Override
            public Long call(Profile profile) {
                return profile.getFb_id();
            }
        });
        mProfilesMap.setIterator(new CompoundIterator<Profile>(ITERATOR_COUNT, mProfilesMap.values()));
        mProfileIterator = (CompoundIterator<Profile>) mProfilesMap.iterator();
        mProfilesChangedObservable = new SwarmObservable();
        getSwarm();
    }

    public ISwarmIterator firstIterator() {
        return getIterator(0);
    }

    public ISwarmIterator secondIterator() {
        return getIterator(1);
    }

    private ISwarmIterator getIterator(int i) {
        SwarmIterator swarmIterator = new SwarmIterator(mProfileIterator.getIterator(i));
        mProfilesChangedObservable.addObserver(swarmIterator);
        return swarmIterator;
    }

    private void getSwarm() {
        IValueStoreService valueStore = AppService.getInstance().getValueStore();
        HashSet<String> keys = new HashSet<String>(Arrays.asList("radius", "min_age", "max_age"));
        Map<String, ?> searchSettings = valueStore.getEntriesAsMap(keys);

        mNetworkService.getSwarm(searchSettings).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()).subscribe(new Action1<Map<Long, Profile>>() {
            @Override
            public void call(Map<Long, Profile> longProfileMap) {
                if (mProfilesMap.addAll(longProfileMap)) {
                    mProfilesChangedObservable._setChanged();
                    mProfilesChangedObservable.notifyObservers();
                }
            }
        });
    }

    public void reloadSwarm() {
//        TODO: reset all
        getSwarm();
    }

    private class SwarmIterator implements ISwarmIterator, Observer {
        private ILazyIterator<Profile> mIterator;
        private IProfileListener mListener;
        private boolean mIsWaitingForProfiles = false;

        private SwarmIterator(ILazyIterator<Profile> it) {
            mIterator = it;
            if (!mIterator.headIsSet() && !mIterator.tryMove()) {
                mIsWaitingForProfiles = true;
            }
        }

        @Override
        public void hi() {
            like(true);
        }

        @Override
        public void bye() {
            like(false);
        }

        private void like(boolean doesLike) {
            Profile other = mIterator.head();
            moveAndSet();
            if (mProfileIterator.aheadCount() < AHEAD_LOADING) {
                getSwarm();
            }

            if (doesLike) {
                mNetworkService.hi(other);
            } else {
                mNetworkService.bye(other);
            }
        }

        private void moveAndSet() {
            if (mIterator.tryMove()) {
                setProfile();
            } else {
                synchronized (SwarmIterator.this) {
                    mIsWaitingForProfiles = true;
                }
            }
        }

        private void setProfile() {
            mListener.setProfile(mIterator.head());
        }

        @Override
        public void setProfileListener(IProfileListener listener) {
            mListener = listener;
            if (mIterator.headIsSet() || mIterator.tryMove()) {
                setProfile();
            }
        }

        @Override
        public void update(Observable observable, Object data) {
            synchronized (SwarmIterator.this) {
                if (mIsWaitingForProfiles) {
                    mIsWaitingForProfiles = false;
                    mIterator.move();
                    setProfile();
                }
            }
        }
    }

    private class SwarmObservable extends Observable {
        void _setChanged() {
            setChanged();
        }
    }
}
