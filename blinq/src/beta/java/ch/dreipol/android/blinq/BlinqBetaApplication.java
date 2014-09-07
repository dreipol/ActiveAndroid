package ch.dreipol.android.blinq;

import android.content.Context;

import ch.dreipol.android.blinq.application.BlinqApplication;
import ch.dreipol.android.blinq.application.ImageCacheService;
import ch.dreipol.android.blinq.application.JsonStoreServiceBuilder;
import ch.dreipol.android.blinq.application.MatchesService;
import ch.dreipol.android.blinq.application.PreferencesValueStore;
import ch.dreipol.android.blinq.services.BetaImageCacheService;
import ch.dreipol.android.blinq.services.BetaNetworkService;
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
import ch.dreipol.android.blinq.services.impl.DatabaseDebugService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.services.impl.LocationService;
import ch.dreipol.android.blinq.services.impl.RuntimeService;
import ch.dreipol.android.blinq.services.model.SearchSettings;
import ch.dreipol.android.dreiworks.ICacheService;
import ch.dreipol.android.dreiworks.JsonStoreName;
import ch.dreipol.android.dreiworks.ServiceBuilder;

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
                return new ServiceBuilder<BetaNetworkService>(BetaNetworkService.class);
            }

            @Override
            public ServiceBuilder<? extends IImageCacheService> imageCacheServiceBuilder() {
                return new ServiceBuilder<BetaImageCacheService>(BetaImageCacheService.class);
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
                return new ServiceBuilder<DatabaseDebugService>(DatabaseDebugService.class);
            }

            @Override
            public ServiceBuilder<? extends ICacheService> jsonCacheServiceBuilder() {
                return new JsonStoreServiceBuilder().
                        addDefault(JsonStoreName.SEARCH_SETTINGS.toString(), SearchSettings.defaultSettings());
            }

        };
    }

}
