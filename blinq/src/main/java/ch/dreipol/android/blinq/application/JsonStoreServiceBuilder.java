package ch.dreipol.android.blinq.application;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.dreiworks.JsonStoreCacheService;
import ch.dreipol.android.dreiworks.ServiceBuilder;
import ch.dreipol.android.dreiworks.jsonstore.CachedModel;

/**
 * Created by melbic on 28/08/14.
 */
public class JsonStoreServiceBuilder extends ServiceBuilder<JsonStoreCacheService> {

    Map<String, ObjectTypePair> defaultMap = new HashMap<String, ObjectTypePair>();

    public JsonStoreServiceBuilder() {
        super(JsonStoreCacheService.class);
    }

    @Override
    public JsonStoreCacheService build(AppService appService) throws IllegalAccessException, InstantiationException {
        JsonStoreCacheService service = super.build(appService);
        for (Map.Entry<String, ObjectTypePair> entry : defaultMap.entrySet()) {
            try {
                CachedModel cachedModel = service.get(entry.getKey(), entry.getValue().mTypeToken);
                if (!cachedModel.doesExist()) {
                    service.put(entry.getKey(), entry.getValue());
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
