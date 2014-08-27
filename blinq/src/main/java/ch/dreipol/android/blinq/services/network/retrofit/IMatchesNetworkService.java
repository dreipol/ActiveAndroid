package ch.dreipol.android.blinq.services.network.retrofit;

import com.google.gson.JsonElement;

import java.util.Map;

import ch.dreipol.android.blinq.services.network.TaskStatus;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by melbic on 18/07/14.
 */
public interface IMatchesNetworkService {
    @POST("/matches/")
    Observable<TaskStatus<JsonElement>> getMatchesTask(@Body Map swarmBody);
}
