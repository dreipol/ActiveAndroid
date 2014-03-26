package ch.dreipol.android.blinq.services.network.retrofit;

import java.util.List;
import java.util.Map;

import ch.dreipol.android.blinq.services.TaskStatus;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by phil on 26.03.14.
 */
public interface PollService {

    @POST("/poll/")
    Observable<List<TaskStatus>> poll(@Body Map pollBody);
}
