package ch.dreipol.android.blinq.services;

import android.content.Context;

/**
 * Created by phil on 22.03.14.
 */
public class AppService {

    private static volatile AppService instance = null;

    private ILocationService mLocationService;
    private IFacebookService mSessionService;
    private IValueStoreService mValueStore;

    private Context mContext;


    private AppService(IServiceConfiguration configuration) {
        setup(configuration);
    }

    public static AppService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BLINQ App Services not initialized. Call initialize before using the service.");
        }
        return instance;
    }

    public static void initialize(IServiceConfiguration configuration) {
        if (instance == null) {
            synchronized (AppService.class) {
                instance = new AppService(configuration);
            }
        } else {
            instance.clear();
            instance.setup(configuration);
        }
    }

    public ILocationService getLocationService() {
        return mLocationService;
    }

    public Context getContext() {
        return mContext;
    }

    private void clear() {
        mLocationService.dispose();
    }


    private void setup(IServiceConfiguration configuration) {
        try {
            mLocationService = configuration.locationService().newInstance();
            mSessionService = configuration.sessionService().newInstance();
            mValueStore = configuration.valueStoreService().newInstance();


        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        mContext = configuration.getContext();

        if (mContext == null) {
            throw new RuntimeException("Cannot work with a null context");
        }

        getLocationService().setup(this);
        getFacebookService().setup(this);
        getValueStore().setup(this);
    }


    public IFacebookService getFacebookService() {
        return mSessionService;
    }

    public IValueStoreService getValueStore() {
        return mValueStore;
    }


}
