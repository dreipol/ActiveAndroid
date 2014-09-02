package ch.dreipol.android.dreiworks.gson;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ch.dreipol.android.blinq.services.model.Photo;
import ch.dreipol.android.dreiworks.GsonHelper;

/**
 * Created by melbic on 02/09/14.
 */
public class PhotoAdapter implements JsonDeserializer<Photo> {
    @Override
    public Photo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson g = GsonHelper.getGSONDeserializationBuilder().create();
        Photo p = g.fromJson(json, Photo.class);
        p.setExpireDate();
        return p;
    }
}
