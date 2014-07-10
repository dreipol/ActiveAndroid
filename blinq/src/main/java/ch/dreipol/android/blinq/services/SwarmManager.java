package ch.dreipol.android.blinq.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.dreipol.android.blinq.util.Bog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by melbic on 10/07/14.
 */
public class SwarmManager implements ISwarmService {


    private final INetworkService mNetworkService;
    private final PublishSubject<Profile> mSwarmSubject;
    private final HashSet<Long> mShowedProfileSet;

    public SwarmManager() {
        mNetworkService = AppService.getInstance().getNetworkService();
        mSwarmSubject = PublishSubject.create();
        mShowedProfileSet = new HashSet<Long>();
        getSwarm();
    }

    @Override
    public Profile next() {
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
        IValueStoreService valueStore = AppService.getInstance().getValueStore();
        HashSet<String> keys = new HashSet<String>(Arrays.asList("radius", "min_age", "max_age"));
        Map<String, ?> searchSettings = valueStore.getEntriesAsMap(keys);
        mNetworkService.getSwarm(searchSettings).count().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Bog.v(Bog.Category.NETWORKING, integer.toString());
                    }
                }
                ,
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Bog.e(Bog.Category.NETWORKING, "", throwable);

                    }
                },
                new Action0() {
                    @Override
                    public void call() {
                        Bog.v(Bog.Category.NETWORKING, "complete" );

                    }
                }
        );
    }
}
