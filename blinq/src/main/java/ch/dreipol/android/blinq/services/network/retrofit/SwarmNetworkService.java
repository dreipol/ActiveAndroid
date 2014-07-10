package ch.dreipol.android.blinq.services.network.retrofit;

import com.google.gson.JsonElement;

import java.util.Map;

import ch.dreipol.android.blinq.services.TaskStatus;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by phil on 26.03.14.
 */
public interface SwarmNetworkService {

    @POST("/swarm/")
    Observable<TaskStatus<JsonElement>> getSwarmTask(@Body Map swarmBody);
}
