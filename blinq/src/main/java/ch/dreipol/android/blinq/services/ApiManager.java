package ch.dreipol.android.blinq.services;

import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by melbic on 14/03/14.
 */
public class ApiManager {

    private static Map map;
    static {
        Map<String, String> aMap = new HashMap<String,String>();
//        map.put();
        map = Collections.unmodifiableMap(aMap);
    }
    private interface ApiManagerService {
        @POST("/swarm")
        Observable<List<Profile>> getSwarm(@Body Map body);
    }

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://api.openweathermap.org/data/2.5")
            .build();
    private static final ApiManagerService apiManager = restAdapter.create(ApiManagerService.class);

    public ApiManager() {
    }

    public Observable<Profile> getSwarm() {
        apiManager.getSwarm(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<Profile>, Observable<Profile>>() {
                    @Override
                    public Observable<Profile> call(List<Profile> profiles) {
                        return Observable.from(profiles);
                    }
                })
                .subscribe(new Action1<Profile>() {
                    @Override
                    public void call(Profile profile) {
                        Log.d("ch.blinq", profile.toString());
                    }
                });

//        return Observable.from(new Observable.OnSubscribeFunc<Profile>() {
//            @Override
//            public Subscription onSubscribe(Observer<? super Profile> observer) {
//                try {
//                    observer.onNext();
//                    observer.onCompleted();
//                } catch (Exception e) {
//                    observer.onError(e);
//                }
//
//                return Subscriptions.empty();
//            }
//        }).subscribeOn(Schedulers.threadPoolForIO());
    }

}