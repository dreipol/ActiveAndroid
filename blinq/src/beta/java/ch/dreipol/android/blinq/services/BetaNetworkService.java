package ch.dreipol.android.blinq.services;

import com.google.gson.Gson;

import ch.dreipol.android.blinq.services.impl.NetworkService;
import retrofit.RestAdapter;

/**
 * Created by phil on 19/08/14.
 */
public class BetaNetworkService extends NetworkService {

    @Override
    protected RestAdapter.Builder getRestBuilder(Gson gson, String serverUrl) {
        RestAdapter.Builder restBuilder = super.getRestBuilder(gson, serverUrl);




        return restBuilder;
    }
}
