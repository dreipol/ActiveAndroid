package ch.dreipol.android.blinq.services.impl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ConnectionSignatureCreator;
import ch.dreipol.android.blinq.services.ICredentials;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.services.INetworkService;
import ch.dreipol.android.blinq.services.Profile;
import ch.dreipol.android.blinq.services.ServerStatus;
import ch.dreipol.android.blinq.services.TaskStatus;
import ch.dreipol.android.blinq.services.network.Pollworker;
import ch.dreipol.android.blinq.services.network.retrofit.PollService;
import ch.dreipol.android.blinq.services.network.retrofit.ProfileService;
import ch.dreipol.android.blinq.services.network.retrofit.SwarmService;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedOutput;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by phil on 26.03.14.
 */
public class NetworkService extends BaseService implements INetworkService {


    private SwarmService mSwarmService;
    private ProfileService mProfileService;
    private Pollworker mPollWorker;


    private void signBodyObject(JsonObject jsonObject) {
        final IFacebookService facebook = getService().getFacebookService();
        final String facebookId = facebook.getFacebookId();
        final String token = facebook.getAccessToken();
        jsonObject.addProperty("userId", facebookId);
        jsonObject.addProperty("token", token);

        String signature = ConnectionSignatureCreator.signatureForCredentials(new ICredentials() {

            @Override
            public String getUserID() {
                return facebookId;
            }

            @Override
            public String getToken() {
                return token;
            }
        });
        jsonObject.addProperty("signature", signature);
    }


    @Override
    public void setup(AppService appService) {
        super.setup(appService);


        Gson gson = getGson();


        String serverUrl = getServerUrl();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(serverUrl)
                .setConverter(new BlinqConverter(gson)).setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mSwarmService = restAdapter.create(SwarmService.class);
        mProfileService = restAdapter.create(ProfileService.class);
        mPollWorker = new Pollworker(restAdapter.create(PollService.class));
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(ServerStatus.class, new ServerStatusAdapter())
                .create();
    }


    @Override
    public Observable<Profile> getSwarm(Map swarmBody) {
        return getRequestObservable(mSwarmService.getSwarmTask(swarmBody), TypeToken.get(Profile.class));
    }

    @Override
    public Observable<Profile> getMe() {
        return getRequestObservable(mProfileService.getMe(new HashMap()), TypeToken.get(Profile.class));
    }

    private <T> Observable<T> getRequestObservable(Observable<TaskStatus<JsonElement>> observable, final TypeToken<T> typeToken) {
//        Observable<FacebookService.FacebookServiceInfo> facebookObservable = this.getService().getFacebookService().subscribeToSessionState().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).filter(new Func1<FacebookService.FacebookServiceInfo, Boolean>() {
//            @Override
//            public Boolean call(FacebookService.FacebookServiceInfo facebookServiceInfo) {
//                return facebookServiceInfo.status == FacebookService.FacebookServiceStatus.LOGGED_IN;
//            }
//        });
//        .zip(facebookObservable, new Func2<TaskStatus<JsonElement>, FacebookService.FacebookServiceInfo, TaskStatus<JsonElement>>() {
//            @Override
//            public TaskStatus<JsonElement> call(TaskStatus<JsonElement> jsonElementTaskStatus, FacebookService.FacebookServiceInfo facebookServiceInfo) {
//                return jsonElementTaskStatus;
//            }
//        })
        final BehaviorSubject<TaskStatus<JsonElement>> behaviorSubject = BehaviorSubject.create(TaskStatus.<JsonElement>initialStatus());
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<TaskStatus<JsonElement>>() {
                    @Override
                    public void call(TaskStatus<JsonElement> profileTaskStatus) {
                        Observable<TaskStatus<JsonElement>> o = mPollWorker.addTaskStatus(profileTaskStatus);
                        o.subscribeOn(Schedulers.io()).subscribe(behaviorSubject);
                    }
                });
        return behaviorSubject.filter(new Func1<TaskStatus<JsonElement>, Boolean>() {
            @Override
            public Boolean call(TaskStatus<JsonElement> taskStatus) {
                return taskStatus.getStatus() == ServerStatus.SUCCESS;
            }
        }).map(new Func1<TaskStatus<JsonElement>, T>() {
            @Override
            public T call(TaskStatus<JsonElement> taskStatus) {
                return getGson().fromJson(taskStatus.getMessage(), typeToken.getType());
            }
        });
    }

    private String getServerUrl() {
//        TODO: make lazy, move key to a central place
        return getService().getRuntimeService().getMetadata("BLINQ_SERVER");
    }


    private class BlinqConverter extends GsonConverter {
        private Gson mGson;

        public BlinqConverter(Gson gson) {
            super(gson);
            mGson = gson;
        }

        @Override
        public TypedOutput toBody(Object object) {

            JsonObject jsonObject = mGson.toJsonTree(object).getAsJsonObject();

            NetworkService.this.signBodyObject(jsonObject);

            return super.toBody(jsonObject);
        }
    }
}
