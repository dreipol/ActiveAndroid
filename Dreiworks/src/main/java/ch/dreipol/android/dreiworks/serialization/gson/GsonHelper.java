package ch.dreipol.android.dreiworks.serialization.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;


/**
 * Created by melbic on 06/03/14.
 */
public abstract class GsonHelper {

    public static GsonBuilder getGsonBuilder(FieldNamingPolicy namingPolicy ) {
        return new GsonBuilder()
                .setExclusionStrategies(new ActiveAndroidIDExclusionStrategy())
                .setFieldNamingStrategy(new AndroidFieldNamingStrategy(namingPolicy))
                .setExclusionStrategies(new GsonExclusionStrategy());
    }

    /**
     * GSON Builder for receiving JSON from a server
     */
    public static GsonBuilder getGSONDeserializationBuilder()
    {
        return getGsonBuilder(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }

    /**
     * GSON Builder for sending JSON to a server
     */
    public static GsonBuilder getGSONSerializationBuilder()
    {
        return getGsonBuilder(FieldNamingPolicy.UPPER_CAMEL_CASE);
    }
}
