package ch.dreipol.android.blinq.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by melbic on 14/03/14.
 */
public class ApiManager {

    private static Map map;

    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("radius", "200");
        aMap.put("userId", "617886513");
        aMap.put("min_age", "18");
        aMap.put("max_age", "69");
        aMap.put("token", "CAADdesunarMBAAg7PYs00JgjYaZBG3nGBoXygRvUrZAZC0xfHm9X6qbYoJ7u9T0ZBZAfZCTjXZAaiO8ZBTqwdkvBv7fo3vuZAIjYIz3VDDZAwR0HbGRJ92mNtyI4R2EvZCnZCfJZBklNZAA9YbPJtkbxR3O0XM4Tfkmsxb6JBrV95t0JVsGOkdqwbZB9I0ZBIJwySmVu98oZD");
        aMap.put("signature", ConnectionSignatureCreator.signatureForCredentials(new ICredentials() {
            @Override
            public String getUserID() {
                return "617886513";
            }

            @Override
            public String getToken() {
                return "CAADdesunarMBAAg7PYs00JgjYaZBG3nGBoXygRvUrZAZC0xfHm9X6qbYoJ7u9T0ZBZAfZCTjXZAaiO8ZBTqwdkvBv7fo3vuZAIjYIz3VDDZAwR0HbGRJ92mNtyI4R2EvZCnZCfJZBklNZAA9YbPJtkbxR3O0XM4Tfkmsxb6JBrV95t0JVsGOkdqwbZB9I0ZBIJwySmVu98oZD";
            }
        }));
        map = Collections.unmodifiableMap(aMap);
    }

    private Subscriber<Observable<TaskStatus>> mPollSubscriber;

    private interface ApiManagerService {
        @POST("/swarm")
        Observable<TaskStatus> getSwarmTask(@Body Map body);

        @POST("/poll")
        Observable<List<Observable>> poll(@Body Map body);
    }

    private static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://beta.server.blinqapp.ch")
            .build();
    private static final ApiManagerService apiManager = restAdapter.create(ApiManagerService.class);

    public ApiManager() {
        mPollSubscriber = new Subscriber<Observable<TaskStatus>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Observable<TaskStatus> taskStatusObservable) {

            }
        };
    }

    public static Observable<Profile> getSwarm() {

        Observable<TaskStatus> swarmStatus = apiManager.getSwarmTask(map)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
//        .poll()
//                .

        poll(swarmStatus);
        return swarmStatus
                .delay(2, TimeUnit.SECONDS)
                .flatMap(new Func1<TaskStatus, Observable<? extends Profile>>() {
                    @Override
                    public Observable<? extends Profile> call(TaskStatus taskStatus) {
                        return null;
                    }
                });
//                .flatMap(new Func1<List<Profile>, Observable<Profile>>() {
//                    @Override
//                    public Observable<Profile> call(List<Profile> profiles) {
//                        return Observable.from(profiles);
//                    }
//                });
    }

    private static Observable poll(Observable observable) {

        Observable.merge(observable, observable)
                .buffer(2, TimeUnit.SECONDS, Schedulers.io())
                .doOnNext(new Action1() {
                    @Override
                    public void call(Object o) {

                    }
                });
        apiManager.poll(null)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
//                .
        return null;
    }


//    public static void example() {
//        Observable o = Observable.create(new Observable.OnSubscribe<JSONObject>() {
//
//            @Override
//            public void call(Subscriber<? super JSONObject> subscriber) {
//                try {
//                    // Do the request
//                    // ...
//                    observer.onNext(jsonResponse);
//                    observer.onCompleted();
//                } catch (Exception e) {
//                    observer.onError(e);
//                }
//
//                return Subscriptions.empty();
//            }
//        }
//    }
//
//    );

}
//}