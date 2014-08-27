package ch.dreipol.android.blinq.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IAccountService;
import ch.dreipol.android.blinq.services.IDatabaseService;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.services.IImageCacheService;
import ch.dreipol.android.blinq.services.ILocationService;
import ch.dreipol.android.blinq.services.IMatchesService;
import ch.dreipol.android.blinq.services.INetworkMethods;
import ch.dreipol.android.blinq.services.IRuntimeService;
import ch.dreipol.android.blinq.services.IServiceConfiguration;
import ch.dreipol.android.blinq.services.IValueStoreService;
import ch.dreipol.android.blinq.services.impl.AccountService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.services.impl.LocationService;
import ch.dreipol.android.blinq.services.impl.NetworkService;
import ch.dreipol.android.blinq.services.impl.RuntimeService;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.blinq.services.impl.DatabaseService;
import ch.dreipol.android.dreiworks.ICacheService;
import ch.dreipol.android.dreiworks.JsonStoreCacheService;

/**
 * Created by phil on 19.03.14.
 */
public class BlinqApplication extends Application implements Application.ActivityLifecycleCallbacks {

    public static final String HOCKEY_APP_IDENTIFIER = "b6515d8c05bcc1ba45256f9dae09cfc9";


    @Override
    public void onCreate() {
        super.onCreate();
        Bog.v(Bog.Category.SYSTEM, "Starting BLINQ: " + getApplicationContext().getPackageName());
        registerActivityLifecycleCallbacks(this);


        AppService.initialize(getConfiguration());
        Bog.v(Bog.Category.SYSTEM, "BLINQ Flavour is: " + AppService.getInstance().getRuntimeService().getFlavour());
    }

    protected IServiceConfiguration getConfiguration() {
        return new IServiceConfiguration() {
            @Override
            public Context getContext() {
                return getApplicationContext();
            }

            @Override
            public Class<? extends ILocationService> locationService() {
                return LocationService.class;
            }

            @Override
            public Class<? extends IRuntimeService> runtimeService() {
                return RuntimeService.class;
            }

            @Override
            public Class<? extends IFacebookService> facebookService() {
                return FacebookService.class;
            }

            @Override
            public Class<? extends IValueStoreService> valueStoreService() {
                return PreferencesValueStore.class;
            }

            @Override
            public Class<? extends INetworkMethods> networkService() {
                return NetworkService.class;
            }

            @Override
            public Class<? extends IImageCacheService> imageCacheService() {
                return ImageCache.class;
            }

            @Override
            public Class<? extends IMatchesService> matchesService() {
                return MatchesService.class;
            }

            @Override
            public Class<? extends IAccountService> accountService() {
                return AccountService.class;
            }

            @Override
            public Class<? extends IDatabaseService> databaseService() {
                return DatabaseService.class;
            }

            @Override
            public Class<? extends ICacheService> jsonCache() {
                return JsonStoreCacheService.class;
            }
        };
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Bog.v(Bog.Category.UI, "Created Activity: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Bog.v(Bog.Category.UI, "Started Activity: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Bog.v(Bog.Category.UI, "Resumed Activity: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Bog.v(Bog.Category.UI, "Paused Activity: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Bog.v(Bog.Category.UI, "Stopped Activity: " + activity.getLocalClassName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Bog.v(Bog.Category.UI, "Saved Activity Instance State: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Bog.v(Bog.Category.UI, "Destroyed Activity: " + activity.getLocalClassName());
    }
}
