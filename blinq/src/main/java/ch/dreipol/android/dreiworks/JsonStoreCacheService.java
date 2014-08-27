package ch.dreipol.android.dreiworks;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.impl.BaseService;
import ch.dreipol.android.dreiworks.jsonstore.AESEncryption;
import ch.dreipol.android.dreiworks.jsonstore.JsonStore;
import ch.dreipol.android.dreiworks.plattform.AndroidStreamProvider;

/**
 * Created by melbic on 26/08/14.
 */
public class JsonStoreCacheService extends BaseService implements IStoreService {
    private HashMap<String, CachedModel> mCacheMap;
    private JsonStore mStore;

    public <T> CachedModel<T> put(String key, T value) throws IOException, IllegalArgumentException {
        synchronized (mCacheMap) {
            CachedModel<T> storedObject = mCacheMap.get(key);
            if (storedObject != null && !storedObject.getClass().isAssignableFrom(value.getClass())) {
                throw new IllegalArgumentException("");
            } else if (storedObject == null) {
                storedObject = new CachedModel<T>();
            }
            mStore.put(key, value);
            storedObject.setCachedObject(value);

            return storedObject;
        }
    }

    public <T> CachedModel<T> get(String key, final Class<T> clazz) throws IOException {
        return getByType(key, clazz);
    }

    private <T> CachedModel<T> getByType(String key, Type type) throws IOException {
        synchronized (mCacheMap) {
            CachedModel<T> storedObject = mCacheMap.get(key);
            if (storedObject == null) {
                T object = mStore.getByType(key, type);
                storedObject = new CachedModel<T>(object);
                mCacheMap.put(key, storedObject);
            }
            return storedObject;
        }
    }

    public <T> CachedModel<T> get(String key, TypeToken<T> type) throws IOException {
        return getByType(key, type.getType());
    }

    public void remove(String key) throws IOException {
        synchronized (mCacheMap) {
            CachedModel cachedModel = mCacheMap.remove(key);
            cachedModel.setRemoved();
            cachedModel.hasChanged();
            mStore.remove(key);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void setup(AppService appService) {
        super.setup(appService);
        mCacheMap = new HashMap<String, CachedModel>();
        mStore = new JsonStore(GsonHelper.getGSONSerializationBuilder().create(), new AndroidStreamProvider(appService.getContext()), new AESEncryption());
    }
}
