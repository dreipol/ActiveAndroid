package ch.dreipol.android.blinq.services;

/**
 * Created by phil on 25.03.14.
 */
public interface IValueStoreService extends IService{

    String get(String key);

    void put(String key, String value);

    boolean has(String key);

    void clear(String key);

}
