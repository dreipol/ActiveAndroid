package ch.dreipol.android.blinq.services.network.retrofit;

import com.google.gson.JsonElement;

import java.util.Map;

import ch.dreipol.android.blinq.services.network.TaskStatus;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by phil on 26.03.14.
 */
public interface ISwarmNetworkService {

    @POST("/swarm/")
    Observable<TaskStatus<JsonElement>> getSwarmTask(@Body Map swarmBody);

    @POST("/hi/")
    Observable<TaskStatus<JsonElement>> getHiTask(@Body Map swarmBody);

    @POST("/bye/")
    Observable<TaskStatus<JsonElement>> getByeTask(@Body Map swarmBody);
}
