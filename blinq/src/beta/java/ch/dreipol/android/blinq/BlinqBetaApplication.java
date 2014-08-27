package ch.dreipol.android.blinq;

import android.content.Context;

import ch.dreipol.android.blinq.application.BlinqApplication;
import ch.dreipol.android.blinq.application.ImageCache;
import ch.dreipol.android.blinq.application.MatchesService;
import ch.dreipol.android.blinq.application.PreferencesValueStore;
import ch.dreipol.android.blinq.services.BetaNetworkService;
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
import ch.dreipol.android.blinq.services.impl.DatabaseDebugService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.services.impl.LocationService;
import ch.dreipol.android.blinq.services.impl.RuntimeService;
import ch.dreipol.android.dreiworks.ICacheService;
import ch.dreipol.android.dreiworks.JsonStoreCacheService;

/**
 * Created by phil on 19/08/14.
 */
public class BlinqBetaApplication extends BlinqApplication {

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
                return BetaNetworkService.class;
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
                return DatabaseDebugService.class;
            }

            @Override
            public Class<? extends ICacheService> jsonCache() {
                return JsonStoreCacheService.class;
            }
        };
    }

}
