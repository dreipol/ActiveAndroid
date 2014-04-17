package ch.dreipol.android.blinq.services.impl;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ch.dreipol.android.blinq.services.ServerStatus;

/**
 * Created by melbic on 27/03/14.
 */
public class ServerStatusAdapter implements JsonDeserializer<ServerStatus> {

    @Override
    public ServerStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        JsonObject jsonObject = json.getAsJsonObject();
//        JsonElement status = jsonObject.get("status");
        ServerStatus serverStatus = ServerStatus.fromString(json.getAsString());
        return serverStatus;
    }
}