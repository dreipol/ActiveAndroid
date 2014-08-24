package ch.dreipol.android.blinq.services.model.facebook;

import com.facebook.model.GraphObject;
import com.google.gson.Gson;

import ch.dreipol.android.dreiworks.serialization.gson.GsonHelper;

/**
 * Created by phil on 22/08/14.
 */
public class FacebookPhotoSource {
    Integer mWidth;
    Integer mHeight;
    String mSource;



    public static FacebookPhotoSource createFromGraph(GraphObject graphObject) {
        Gson gson = GsonHelper.getFacebookGSONDeserializationBuilder().create();
        return gson.fromJson(graphObject.getInnerJSONObject().toString(), FacebookPhotoSource.class);

    }

    public String getSource() {
        return mSource;
    }
}
