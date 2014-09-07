package ch.dreipol.android.blinq.services;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by phil on 25.03.14.
 */
public interface IValueStoreService extends IService {

    String get(String key);

    int getInt(String key);
    void put(String key, int value);

    void put(String key, String value);

    boolean has(String key);

    void clear(String key);

    /**
     * @param keys : Collection of the keys
     * @return a map {"key":"value"} if a key is not stored in the store a null should be returned as value.
     */
//    TODO: @Phil machen null values Sinn?
    Map<String, ?> getEntriesAsMap(Set<String> keys);
}
