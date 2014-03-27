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
public interface ProfileService {
    @POST("/me/")
    Observable<TaskStatus<JsonElement>> getMe(@Body Map meBody);
}
