package ch.dreipol.android.blinq.services.impl;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ConnectionSignatureCreator;
import ch.dreipol.android.blinq.services.DeviceInformation;
import ch.dreipol.android.blinq.services.ICredentials;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.services.INetworkMethods;
import ch.dreipol.android.blinq.services.model.GenderInterests;
import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.services.ServerStatus;
import ch.dreipol.android.blinq.services.network.TaskStatus;
import ch.dreipol.android.blinq.services.model.SettingsProfile;
import ch.dreipol.android.blinq.services.network.Pollworker;
import ch.dreipol.android.blinq.services.network.UploadProfile;
import ch.dreipol.android.blinq.services.network.retrofit.IMatchesNetworkService;
import ch.dreipol.android.blinq.services.network.retrofit.SwarmNetworkService;
import ch.dreipol.android.blinq.services.network.retrofit.PollService;
import ch.dreipol.android.blinq.services.network.retrofit.ProfileService;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.blinq.util.gson.DateTypeAdapter;
import ch.dreipol.android.blinq.util.gson.GenderInterestsAdapter;
import ch.dreipol.android.dreiworks.GsonHelper;
import ch.dreipol.android.dreiworks.JsonStoreName;
import ch.dreipol.android.dreiworks.gson.LatLonAdapter;
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
    private DeviceInformation mDeviceInfo;
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
        jsonObject.add("device", getGson().toJsonTree(mDeviceInfo));
    }

    private JsonElement getDeviceMap() {

        return null;
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
        mDeviceInfo = appService.getRuntimeService().getDeviceInformation();
//        mDeviceInformation = appService.getRuntimeService()
    }

    protected RestAdapter.Builder getRestBuilder(Gson gson, String serverUrl) {
        return new RestAdapter.Builder()
                .setEndpoint(serverUrl)
                .setConverter(new BlinqConverter(gson)).setLogLevel(RestAdapter.LogLevel.FULL);
    }

    protected static Gson getGson() {
        return GsonHelper.getGSONDeserializationBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .registerTypeAdapter(ServerStatus.class, new ServerStatusAdapter())
                .registerTypeAdapter(GenderInterests.class, new GenderInterestsAdapter())
                .registerTypeAdapter(Location.class, new LatLonAdapter())
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

                return p.getFbId();
            }
        });
    }

    @Override
    public Observable<Profile> hi(Profile other) {
        Map m = new HashMap();
        m.put("otherId", other.getFbId());

        return getRequestObservable(mSwarmNetworkService.getHiTask(m), Profile.class);
    }

    @Override
    public void bye(Profile other) {
        Map m = new HashMap();
        final Long fb_id = other.getFbId();
        m.put("otherId", fb_id);

        mSwarmNetworkService.getByeTask(m).subscribe(new Action1<TaskStatus<JsonElement>>() {
            @Override
            public void call(TaskStatus<JsonElement> taskStatus) {
                Bog.v(Bog.Category.NETWORKING, "Said bye to " + fb_id);
            }
        });
    }

    @Override
    public void getMe() {
        getRequestObservable(mProfileService.getMe(new HashMap()), TypeToken.get(SettingsProfile.class))
                .flatMap(new Func1<SettingsProfile, Observable<?>>() {
                             @Override
                             public Observable<SettingsProfile> call(SettingsProfile settingsProfile) {
                                 getService().getJsonCacheService().putToObservable(JsonStoreName.SETTINGS_PROFILE.toString(), settingsProfile);
                                 return Observable.empty();
                             }
                         }

                ).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {

            }
        });
    }


    @Override
    public Observable<SettingsProfile> signup() {
        return getRequestObservable(mProfileService.signup(new HashMap()), TypeToken.get(SettingsProfile.class)).flatMap(new Func1<SettingsProfile, Observable<? extends SettingsProfile>>() {
            @Override
            public Observable<SettingsProfile> call(SettingsProfile settingsProfile) {
                return getService().getJsonCacheService().putToObservable(JsonStoreName.SETTINGS_PROFILE.toString(), settingsProfile);
            }
        });
    }


    @Override
    public void update(UploadProfile profile) {

        getRequestObservable(mProfileService.update(profile), TypeToken.get(SettingsProfile.class)).flatMap(new Func1<SettingsProfile, Observable<? extends SettingsProfile>>() {
            @Override
            public Observable<SettingsProfile> call(SettingsProfile settingsProfile) {
                return getService().getJsonCacheService().putToObservable(JsonStoreName.SETTINGS_PROFILE.toString(), settingsProfile);
            }
        });
    }

    @Override
    public void loadMatches() {
        getService().getMatchesService().loadMatches(getRequestObservable(mMatchesNetworkService.getMatchesTask(new HashMap()), new TypeToken<ArrayList<Match>>() {
        }));
    }

    private <T> Observable<T> getRequestObservable(Observable<TaskStatus<JsonElement>> observable, final TypeToken<T> typeToken) {

        return getRequestObservable(observable, typeToken.getType());
    }

    private <T> Observable<T> getRequestObservable(Observable<TaskStatus<JsonElement>> observable, final Type type) {
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
                return getGson().fromJson(taskStatus.getMessage(), type);
            }
        });
    }

    private String getServerUrl() {
//        TODO: make lazy, move key to a central place
        return getService().getRuntimeService().getMetadataString("BLINQ_SERVER");
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
