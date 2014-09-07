package ch.dreipol.android.blinq.services.network.retrofit;

import com.google.gson.JsonElement;

import java.util.Map;

import ch.dreipol.android.blinq.services.model.SettingsProfile;
import ch.dreipol.android.blinq.services.network.TaskStatus;
import ch.dreipol.android.blinq.services.network.UploadProfile;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by phil on 26.03.14.
 */
public interface IProfileNetworkService {
    @POST("/me/")
    Observable<TaskStatus<JsonElement>> getMe(@Body Map meBody);
    @POST("/sign_up/")
    Observable<TaskStatus<JsonElement>> signup(@Body Map meBody);
    @POST("/update/")
    Observable<TaskStatus<JsonElement>> update(@Body UploadProfile profile);
}
