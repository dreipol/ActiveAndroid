package ch.dreipol.android.dreiworks.jsonstore;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by melbic on 26/08/14.
 */
public interface IJsonStore {
    public <T> void put(String key, T value) throws IOException;

    public <T> T get(String key, Class<T> clazz) throws IOException;

    public <T> T get(String key, TypeToken<T> type) throws IOException;

    public <T> T getByType(String key, Type type) throws IOException;

    public void remove(String key);

    void clear();
}
