package ch.dreipol.android.blinq.services;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import ch.dreipol.android.blinq.application.BlinqApplicationFlavour;
import ch.dreipol.android.blinq.util.Bog;

/**
 * Created by phil on 22.03.14.
 */
public class AppService {

    private static volatile AppService instance = null;

    private ILocationService mLocationService;
    private IFacebookService mSessionService;
    private IValueStoreService mValueStore;
    private INetworkService mNetworkService;

    private Context mContext;


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


    public BlinqApplicationFlavour getFlavour() {
        return BlinqApplicationFlavour.valueOf(getMetadata("BLINQ_FLAVOUR"));
    }

    public String getMetadata(String key) {

        String result = "unknown value";
        try {
            ApplicationInfo ai = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            result = bundle.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            Bog.e(Bog.Category.SYSTEM, "Could not get Metadata", e);
        }
        return result;
    }

    public IFacebookService getFacebookService() {
        return mSessionService;
    }

    public IValueStoreService getValueStore() {
        return mValueStore;
    }


    public INetworkService getNetworkService() {
        return mNetworkService;
    }

    private void setup(IServiceConfiguration configuration) {
        try {
            mLocationService = configuration.locationService().newInstance();
            mSessionService = configuration.sessionService().newInstance();
            mValueStore = configuration.valueStoreService().newInstance();
            mNetworkService = configuration.networkService().newInstance();

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
        getNetworkService().setup(this);
    }


    private void clear() {
        mLocationService.dispose();
    }


    private AppService(IServiceConfiguration configuration) {
        setup(configuration);
    }
}
