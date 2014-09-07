package ch.dreipol.android.dreiworks;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import ch.dreipol.android.dreiworks.activeandroid.gson.ActiveAndroidIDExclusionStrategy;
import ch.dreipol.android.dreiworks.serialization.gson.AndroidFieldNamingStrategy;
import ch.dreipol.android.dreiworks.serialization.gson.GsonExclusionStrategy;


/**
 * Created by melbic on 06/03/14.
 */
public abstract class GsonHelper {

    public static GsonBuilder getDefaultBuilder(FieldNamingPolicy namingPolicy) {
        return new GsonBuilder()
                .setFieldNamingStrategy(new AndroidFieldNamingStrategy(namingPolicy))
                .setExclusionStrategies(new GsonExclusionStrategy());
    }

    public static GsonBuilder getActiveAndroidBuilder(FieldNamingPolicy namingPolicy) {
        return getDefaultBuilder(namingPolicy).setExclusionStrategies(new ActiveAndroidIDExclusionStrategy());
    }

    /**
     * GSON Builder for receiving JSON from a server
     */
    public static GsonBuilder  getGSONDeserializationBuilder() {
        return getActiveAndroidBuilder(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }

    /**
     * GSON Builder for receiving JSON from facebook
     */
    public static GsonBuilder getFacebookGSONDeserializationBuilder() {
        return getDefaultBuilder(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    }

    /**
     * GSON Builder for sending JSON to a server
     */
    public static GsonBuilder getGSONSerializationBuilder() {
        return getActiveAndroidBuilder(FieldNamingPolicy.UPPER_CAMEL_CASE);
    }
}
