package ch.dreipol.android.blinq.services.impl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ConnectionSignatureCreator;
import ch.dreipol.android.blinq.services.ICredentials;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.services.INetworkMethods;
import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.services.ServerStatus;
import ch.dreipol.android.blinq.services.TaskStatus;
import ch.dreipol.android.blinq.services.network.Pollworker;
import ch.dreipol.android.blinq.services.network.retrofit.IMatchesNetworkService;
import ch.dreipol.android.blinq.services.network.retrofit.SwarmNetworkService;
import ch.dreipol.android.blinq.services.network.retrofit.PollService;
import ch.dreipol.android.blinq.services.network.retrofit.ProfileService;
import ch.dreipol.android.blinq.util.Bog;
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
public class NetworkService extends BaseService implements INetworkMethods {

    private IMatchesNetworkService mMatchesNetworkService;
    private SwarmNetworkService mSwarmNetworkService;
    private ProfileService mProfileService;
    private Pollworker mPollWorker;
    public static final TypeToken PROFILE_COLLECTION_TYPE_TOKEN = new TypeToken<Collection<Profile>>() {
    };


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
        RestAdapter restAdapter = getRestBuilder(gson, serverUrl)
                .build();

        mSwarmNetworkService = restAdapter.create(SwarmNetworkService.class);
        mProfileService = restAdapter.create(ProfileService.class);
        mMatchesNetworkService = restAdapter.create(IMatchesNetworkService.class);
        mPollWorker = new Pollworker(restAdapter.create(PollService.class));
    }

    protected RestAdapter.Builder getRestBuilder(Gson gson, String serverUrl) {
        return new RestAdapter.Builder()
                .setEndpoint(serverUrl)
                .setConverter(new BlinqConverter(gson)).setLogLevel(RestAdapter.LogLevel.FULL);
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(ServerStatus.class, new ServerStatusAdapter())
                .create();
    }


    @Override
    public Observable<Map<Long, Profile>> getSwarm(Map swarmBody) {
        return getRequestObservable(mSwarmNetworkService.getSwarmTask(swarmBody), PROFILE_COLLECTION_TYPE_TOKEN).flatMap(new Func1<Collection<Profile>, Observable<Profile>>() {
            @Override
            public Observable<Profile> call(Collection<Profile> collection) {
                return Observable.from(collection);
            }
        }).toMap(new Func1<Profile, Long>() {
            @Override
            public Long call(Profile p) {
                return p.getFb_id();
            }
        });
    }

    @Override
    public Observable<Profile> hi(Profile other) {
        Map m = new HashMap();
        m.put("otherId", other.getFb_id());

        return getRequestObservable(mSwarmNetworkService.getHiTask(m), TypeToken.get(Profile.class));
    }

    @Override
    public void bye(Profile other) {
        Map m = new HashMap();
        final Long fb_id = other.getFb_id();
        m.put("otherId", fb_id);

        mSwarmNetworkService.getByeTask(m).subscribe(new Action1<TaskStatus<JsonElement>>() {
            @Override
            public void call(TaskStatus<JsonElement> taskStatus) {
                Bog.v(Bog.Category.NETWORKING, "Said bye to " + fb_id);
            }
        });
    }

    @Override
    public Observable<Profile> getMe() {
        return getRequestObservable(mProfileService.getMe(new HashMap()), TypeToken.get(Profile.class));
    }

    @Override
    public void loadMatches() {
        getService().getMatchesService().loadMatches(getRequestObservable(mMatchesNetworkService.getMatchesTask(new HashMap()), new TypeToken<Collection<Match>>() {
        }));
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
