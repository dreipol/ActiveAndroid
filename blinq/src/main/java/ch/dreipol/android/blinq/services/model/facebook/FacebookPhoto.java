package ch.dreipol.android.blinq.services.model.facebook;

import com.facebook.model.GraphObject;
import com.google.gson.Gson;

import java.util.Collection;

import ch.dreipol.android.blinq.services.model.ILoadable;
import ch.dreipol.android.dreiworks.serialization.gson.GsonHelper;

/**
 * Created by phil on 22/08/14.
 */
public class FacebookPhoto implements ILoadable {
    String mId;
    String mPicture;
    String mIcon;
    Collection<FacebookPhotoSource> mImages;
    private String mPhoto;

    public static FacebookPhoto createFromGraph(GraphObject graphObject) {
        Gson gson = GsonHelper.getFacebookGSONDeserializationBuilder().create();
        return gson.fromJson(graphObject.getInnerJSONObject().toString(), FacebookPhoto.class);

    }

    public FacebookPhoto(String id) {
        mId = id;
    }

    public String getPicture() {
        return mPicture;
    }

    public String getId() {
        return mId;
    }
}
