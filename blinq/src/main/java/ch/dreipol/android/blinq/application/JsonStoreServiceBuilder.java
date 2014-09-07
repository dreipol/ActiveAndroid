package ch.dreipol.android.blinq.application;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.model.GenderInterests;
import ch.dreipol.android.blinq.util.gson.GenderInterestsAdapter;
import ch.dreipol.android.dreiworks.GsonHelper;
import ch.dreipol.android.dreiworks.JsonStoreCacheService;
import ch.dreipol.android.dreiworks.ServiceBuilder;
import ch.dreipol.android.dreiworks.jsonstore.CachedModel;

/**
 * Created by melbic on 28/08/14.
 */
public class JsonStoreServiceBuilder extends ServiceBuilder<JsonStoreCacheService> {

    private GsonBuilder mGsonBuilder;
    Map<String, ObjectTypePair> defaultMap = new HashMap<String, ObjectTypePair>();

    public JsonStoreServiceBuilder() {
        super(JsonStoreCacheService.class);
        mGsonBuilder = GsonHelper.getGSONSerializationBuilder();
    }

    @Override
    public JsonStoreCacheService build(AppService appService) throws IllegalAccessException, InstantiationException {
        JsonStoreCacheService service = new JsonStoreCacheService();
        service.setup(appService, mGsonBuilder.create());
        for (Map.Entry<String, ObjectTypePair> entry : defaultMap.entrySet()) {
            try {
                CachedModel cachedModel = service.get(entry.getKey(), entry.getValue().mTypeToken);
                if (!cachedModel.doesExist()) {
                    service.put(entry.getKey(), entry.getValue().object);
                }
            } catch (IOException e) {
                throw new InstantiationException("Building the JsonCacheService failed. " + e.toString());
            }
        }
        return service;
    }

    public <T> JsonStoreServiceBuilder addDefault(String key, T value) {
        defaultMap.put(key, new ObjectTypePair(value));
        return this;
    }

    public <T> JsonStoreServiceBuilder addDefault(String key, T value, TypeToken<T> token) {
        defaultMap.put(key, new ObjectTypePair(value, token));
        return this;
    }

    public JsonStoreServiceBuilder registerGsonTypeAdapter(Type type, Object typeAdapter) {
        mGsonBuilder = mGsonBuilder.registerTypeAdapter(type, typeAdapter);
        return this;
    }

    class ObjectTypePair<T> {

        TypeToken<T> mTypeToken;
        T object;

        public ObjectTypePair(T t, TypeToken<T> token) {
            object = t;
            mTypeToken = token;
        }

        public ObjectTypePair(T t) {
            this(t, (TypeToken<T>) TypeToken.get(t.getClass()));
        }
    }
}
