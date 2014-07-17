package ch.dreipol.android.blinq.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.dreiworks.collections.LinkedSetList;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by melbic on 10/07/14.
 */
public class SwarmManager implements ISwarmService {


    private final INetworkService mNetworkService;
    private final LinkedSetList<Long, Profile> mProfilesList;
    private final Iterator<Profile> mIterator;
    private IValueStoreService mValueStore;

    public SwarmManager() {
        mNetworkService = AppService.getInstance().getNetworkService();
        mProfilesList = new LinkedSetList<Long, Profile>(new Func1<Profile, Long>() {
            @Override
            public Long call(Profile profile) {
                return profile.getFb_id();
            }
        });
        mIterator = mProfilesList.iterator();
        getSwarm();
    }

    @Override
    public Profile next() {
        mIterator.next();
//        mSwarmSubject.toBlocking().toFuture()
        return null;
    }

    @Override
    public void hi() {
//        TODO: say hi
    }

    @Override
    public void bye() {
//        TODO: say bye

    }

    private void getSwarm() {
        mValueStore = AppService.getInstance().getValueStore();
        HashSet<String> keys = new HashSet<String>(Arrays.asList("radius", "min_age", "max_age"));
        Map<String, ?> searchSettings = mValueStore.getEntriesAsMap(keys);

        mNetworkService.getSwarm(searchSettings).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()).subscribe(new Action1<Map<Long, Profile>>() {
            @Override
            public void call(Map<Long, Profile> longProfileMap) {
                mProfilesList.addAll(longProfileMap);
            }
        });
    }
}
