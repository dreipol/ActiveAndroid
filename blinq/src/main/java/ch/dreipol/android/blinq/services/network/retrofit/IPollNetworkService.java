package ch.dreipol.android.blinq.services.network.retrofit;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import ch.dreipol.android.blinq.services.network.TaskStatus;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by phil on 26.03.14.
 */
public interface IPollNetworkService {

    @POST("/poll/")
    Observable<List<TaskStatus<JsonElement>>> poll(@Body Map pollBody);
}
