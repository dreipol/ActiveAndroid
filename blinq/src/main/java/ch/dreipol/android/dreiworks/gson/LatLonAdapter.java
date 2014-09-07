package ch.dreipol.android.dreiworks.gson;

import android.location.Location;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by melbic on 01/09/14.
 */
public class LatLonAdapter implements JsonSerializer<Location> {
    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        double lat = src.getLatitude();
        double lon = src.getLongitude();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("lat", lat);
        jsonObject.addProperty("long", lon);
        return jsonObject;
    }
}
