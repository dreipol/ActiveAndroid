package ch.dreipol.android.dreiworks;


import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import ch.dreipol.android.blinq.services.IService;
import ch.dreipol.android.dreiworks.jsonstore.CachedModel;

/**
 * Created by melbic on 26/08/14.
 */
public interface ICacheService extends IService{
    <T> CachedModel<T> put(String key, T value) throws IOException, IllegalArgumentException;

    <T> CachedModel<T> get(String key, Class<T> clazz) throws IOException;

    <T> CachedModel<T> get(String key, TypeToken<T> type) throws IOException;

    void remove(String key) throws IOException;

    <T> rx.Observable<T> getObservable(String key, Class<T> clazz);

    <T> rx.Observable<T> getObservable(String key, TypeToken<T> type);

    <T> rx.Observable<T> putToObservable(String key, T object);

    void clear();
}
