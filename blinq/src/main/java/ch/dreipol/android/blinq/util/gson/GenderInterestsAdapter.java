package ch.dreipol.android.blinq.util.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

import ch.dreipol.android.blinq.services.model.GenderInterests;

/**
 * Created by melbic on 25/08/14.
 */
public class GenderInterestsAdapter implements JsonSerializer<GenderInterests>, JsonDeserializer<GenderInterests> {
    @Override
    public GenderInterests deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String[] deserialized = context.deserialize(json, String[].class);
        JsonArray jsonArray = json.getAsJsonArray();
        GenderInterests interests = null;
        if (jsonArray.size() > 1) {
            interests = GenderInterests.BOTH;
        } else if (jsonArray.get(0).toString().equalsIgnoreCase("f")) {
            interests = GenderInterests.FEMALE;
        } else {
            interests = GenderInterests.MALE;
        }
        return interests;
    }

    @Override
    public JsonElement serialize(GenderInterests src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.toArray(), String[].class);
    }
}
