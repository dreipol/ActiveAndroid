package ch.dreipol.android.blinq.services;

import android.content.Context;

import ch.dreipol.android.dreiworks.ICacheService;
import ch.dreipol.android.dreiworks.ServiceBuilder;

/**
 * Created by phil on 22.03.14.
 */
public interface IServiceConfiguration {

    Context getContext();

   ServiceBuilder<? extends ILocationService> locationServiceBuilder();

   ServiceBuilder<? extends IRuntimeService> runtimeServiceBuilder();

   ServiceBuilder<? extends IFacebookService> facebookServiceBuilder();

   ServiceBuilder<? extends IValueStoreService> valueStoreServiceBuilder();

   ServiceBuilder<? extends INetworkMethods> networkServiceBuilder();

   ServiceBuilder<? extends IImageCacheService> imageCacheServiceBuilder();

   ServiceBuilder<? extends IMatchesService> matchesServiceBuilder();

   ServiceBuilder<? extends IAccountService> accountServiceBuilder();

   ServiceBuilder<? extends IDatabaseService> databaseServiceBuilder();

   ServiceBuilder<? extends ICacheService> jsonCacheServiceBuilder();
}
