package ch.dreipol.android.blinq.services;

import java.util.HashMap;
import java.util.List;

import ch.dreipol.android.blinq.Profile;
import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by melbic on 14/03/14.
 */
public class ApiManager {

    private static HashMap map;

    private interface ApiManagerService {
        @POST("/swarm")
        List<Profile> getSwarm(@Body HashMap body );
    }

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setServer("http://api.openweathermap.org/data/2.5")
            .build();
    private static final ApiManagerService apiManager = restAdapter.create(ApiManagerService.class);

//    public static Observable<Profile> getSwarm(final String city) {
//        return Observable.from(new Observable.OnSubscribeFunc<Profile>() {
//            @Override
//            public Subscription onSubscribe(Observer<? super Profile> observer) {
//                try {
//                    observer.onNext(apiManager.getSwarm(map));
//                    observer.onCompleted();
//                } catch (Exception e) {
//                    observer.onError(e);
//                }
//
//                return Subscriptions.empty();
//            }
//        }).subscribeOn(Schedulers.threadPoolForIO());
//    }

}