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
import ch.dreipol.android.blinq.services.model.SearchSettings;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.blinq.services.impl.DatabaseService;
import ch.dreipol.android.dreiworks.ICacheService;
import ch.dreipol.android.dreiworks.JsonStoreName;
import ch.dreipol.android.dreiworks.ServiceBuilder;

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
            public ServiceBuilder<? extends ch.dreipol.android.blinq.services.ILocationService> locationServiceBuilder() {
                return new ServiceBuilder<LocationService>(LocationService.class);
            }

            @Override
            public ServiceBuilder<? extends IRuntimeService> runtimeServiceBuilder() {
                return new ServiceBuilder<RuntimeService>(RuntimeService.class);
            }

            @Override
            public ServiceBuilder<? extends IFacebookService> facebookServiceBuilder() {
                return new ServiceBuilder<FacebookService>(FacebookService.class);
            }

            @Override
            public ServiceBuilder<? extends IValueStoreService> valueStoreServiceBuilder() {
                return new ServiceBuilder<PreferencesValueStore>(PreferencesValueStore.class);
            }

            @Override
            public ServiceBuilder<? extends INetworkMethods> networkServiceBuilder() {
                return new ServiceBuilder<NetworkService>(NetworkService.class);
            }

            @Override
            public ServiceBuilder<? extends IImageCacheService> imageCacheServiceBuilder() {
                return new ServiceBuilder<ImageCacheService>(ImageCacheService.class);
            }

            @Override
            public ServiceBuilder<? extends IMatchesService> matchesServiceBuilder() {
                return new ServiceBuilder<MatchesService>(MatchesService.class);
            }

            @Override
            public ServiceBuilder<? extends IAccountService> accountServiceBuilder() {
                return new ServiceBuilder<AccountService>(AccountService.class);
            }

            @Override
            public ServiceBuilder<? extends IDatabaseService> databaseServiceBuilder() {
                return new ServiceBuilder<DatabaseService>(DatabaseService.class);
            }

            @Override
            public ServiceBuilder<? extends ICacheService> jsonCacheServiceBuilder() {
                return new JsonStoreServiceBuilder().
                        addDefault(JsonStoreName.SEARCH_SETTINGS.toString(), SearchSettings.defaultSettings());
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
