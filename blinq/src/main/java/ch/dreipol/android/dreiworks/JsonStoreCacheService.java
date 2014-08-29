package ch.dreipol.android.dreiworks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Observer;
import java.util.concurrent.Callable;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.impl.BaseService;
import ch.dreipol.android.blinq.services.model.GenderInterests;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.blinq.util.gson.GenderInterestsAdapter;
import ch.dreipol.android.dreiworks.jsonstore.AESEncryption;
import ch.dreipol.android.dreiworks.jsonstore.CachedModel;
import ch.dreipol.android.dreiworks.jsonstore.JsonStore;
import ch.dreipol.android.dreiworks.plattform.AndroidStreamProvider;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by melbic on 26/08/14.
 */
public class JsonStoreCacheService extends BaseService implements ICacheService {
    private HashMap<String, CachedModel> mCacheMap;
    private JsonStore mStore;

    @Override
    public <T> CachedModel<T> put(String key, T value) throws IOException, IllegalArgumentException {
        synchronized (mCacheMap) {
            CachedModel<T> storedObject = mCacheMap.get(key);
            if (storedObject == null) {
                storedObject = new CachedModel<T>();
            }
            mStore.put(key, value);
            storedObject.setCachedObject(value);

            return storedObject;
        }
    }

    @Override
    public <T> CachedModel<T> get(String key, final Class<T> clazz) throws IOException {
        return getByType(key, clazz);
    }

    @Override
    public <T> CachedModel<T> get(String key, TypeToken<T> type) throws IOException {
        return getByType(key, type.getType());
    }

    @Override
    public void remove(String key) throws IOException {
        synchronized (mCacheMap) {
            CachedModel cachedModel = mCacheMap.remove(key);
            cachedModel.setRemoved();
            cachedModel.hasChanged();
            mStore.remove(key);
        }
    }

    @Override
    public <T> Observable<T> getObservable(final String key, final Class<T> clazz) {
        return getObservableByType(key, clazz);
    }

    @Override
    public <T> Observable<T> getObservable(final String key, final TypeToken<T> type) {
        return getObservableByType(key, type.getType());
    }

    @Override
    public <T> Observable<T> putToObservable(String key, T object) {
        try {
            CachedModel<T> model = put(key, object);
            return Observable.create(new OnSubscribeObserver<T>(model));
        } catch (IOException e) {
            return Observable.error(e);
        }
    }

    @Override
    /**
     * Clears the cache and removes all the files. The defaults values aren't restored!
     */
    public void clear() {
        mCacheMap.clear();
        mStore.clear();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void setup(AppService appService) {
        setup(appService, GsonHelper.getGSONSerializationBuilder().create());
    }

    public void setup(AppService appService, Gson gson) {
        super.setup(appService);
        mCacheMap = new HashMap<String, CachedModel>();
        mStore = new JsonStore(gson, new AndroidStreamProvider(appService.getContext(), "JSONSTORE"), new AESEncryption());
    }


    protected <T> Observable<T> getObservableByType(final String key, final Type type) {
        Observable<T> o = Observable.empty();
        try {
            CachedModel<T> model = (CachedModel<T>) getByType(key, type);
            o = Observable.create(new OnSubscribeObserver<T>(model));
        } catch (IOException e) {
            Bog.e(Bog.Category.SYSTEM, e.toString());
            o = Observable.error(e);
        } finally {
            return o;
        }
    }

    private <T> CachedModel<T> getByType(String key, Type type) throws IOException {
        synchronized (mCacheMap) {
            CachedModel<T> storedObject = (CachedModel<T>) mCacheMap.get(key);
            if (storedObject == null) {
                storedObject = new CachedModel<T>();
                mCacheMap.put(key, storedObject);
                try {
                    T object = mStore.getByType(key, type);
                    storedObject.setCachedObject(object);
                } catch (FileNotFoundException e) {
                    Bog.v(Bog.Category.SYSTEM, "File " + key + " does not exist.");
                }
            }
            return storedObject;
        }
    }

    class OnSubscribeObserver<T> implements Observable.OnSubscribe<T>, Observer {

        private Subscriber<? super T> mSubscriber;
        private CachedModel<T> mCachedModel;

        public OnSubscribeObserver(CachedModel<T> cachedModel) {
            super();
            mCachedModel = cachedModel;
            mCachedModel.addObserver(this);
        }

        @Override
        public void call(Subscriber<? super T> subscriber) {
            mSubscriber = subscriber;
            if (mCachedModel.doesExist()) {
                subscriber.onNext(mCachedModel.getObject());
            }
        }

        //TODO: Don't think the update works correct
        @Override
        public void update(java.util.Observable observable, Object data) {

            if (mSubscriber != null && observable.equals(mCachedModel)) {
                if (mCachedModel.isRemoved()) {
                    mSubscriber.onCompleted();
                } else {
                    mSubscriber.onNext(mCachedModel.getObject());
                }
            } else if (mSubscriber == null || mSubscriber.isUnsubscribed()) {
                observable.deleteObserver(this);
            }
        }
    }
}
