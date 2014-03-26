package ch.dreipol.android.blinq.services.impl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;
import java.util.HashMap;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ConnectionSignatureCreator;
import ch.dreipol.android.blinq.services.ICredentials;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.services.INetworkService;
import ch.dreipol.android.blinq.services.TaskStatus;
import ch.dreipol.android.blinq.services.network.retrofit.ProfileService;
import ch.dreipol.android.blinq.services.network.retrofit.SwarmService;
import ch.dreipol.android.blinq.util.Bog;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedOutput;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by phil on 26.03.14.
 */
public class NetworkService extends BaseService implements INetworkService {


    private SwarmService mSwarmService;
    private ProfileService mProfileService;


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


        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();


        String serverUrl = getServerUrl();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(serverUrl)
                .setConverter(new BlinqConverter(gson)).setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();

        mSwarmService = restAdapter.create(SwarmService.class);
        mProfileService = restAdapter.create(ProfileService.class);
    }


    @Override
    public void getSwarm() {

    }

    @Override
    public void getMe() {
//        pollWorker.poll(mProfileService.getMe(new HashMap())).subscribe().
        mProfileService.getMe(new HashMap())
                .observeOn(Schedulers.io())

                .subscribe(new Action1<TaskStatus>() {
                    @Override
                    public void call(TaskStatus taskStatus) {
                        Bog.v(Bog.Category.NETWORKING, taskStatus.toString());
                    }
                });


    }

    private String getServerUrl() {
//        TODO: make lazy, move key to a central place
        return getService().getMetadata("BLINQ_SERVER");
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
