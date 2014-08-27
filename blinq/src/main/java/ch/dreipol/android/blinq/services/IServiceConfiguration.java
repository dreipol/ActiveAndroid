package ch.dreipol.android.blinq.services;

import android.content.Context;

import ch.dreipol.android.dreiworks.ICacheService;
import ch.dreipol.android.dreiworks.ServiceBuilder;

/**
 * Created by phil on 22.03.14.
 */
public interface IServiceConfiguration {

    Context getContext();

    Class<? extends ILocationService> locationService();

    ServiceBuilder<? extends IRuntimeService> runtimeServiceBuilder();

    Class<? extends IFacebookService> facebookService();

    Class<? extends IValueStoreService> valueStoreService();

    Class<? extends INetworkMethods> networkService();

    Class<? extends IImageCacheService> imageCacheService();

    Class<? extends IMatchesService> matchesService();

    Class<? extends IAccountService> accountService();

    Class<? extends IDatabaseService> databaseService();

    Class<? extends ICacheService> jsonCache();
}
