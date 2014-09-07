package ch.dreipol.android.blinq.services.network.retrofit;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import ch.dreipol.android.blinq.services.network.TaskStatus;
import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by melbic on 02/09/14.
 */
public interface IPhotoNetworkService {
    @POST("/renew_photosource/")
    Observable<TaskStatus<JsonElement>> renewPhotoSource(@Body Map body);
    @POST("/remove_photo/")
    Observable<TaskStatus<JsonElement>> removePhoto(@Body Map body);
    @POST("/update_photo/")
    Observable<TaskStatus<JsonElement>> updatePhoto(@Body Map body);
}
