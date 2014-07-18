package ch.dreipol.android.blinq.services;

import android.content.Context;

/**
 * Created by phil on 22.03.14.
 */
public interface IServiceConfiguration {

    Context getContext();

    Class<? extends ILocationService> locationService();

    Class<? extends IRuntimeService> runtimeService();

    Class<? extends IFacebookService> facebookService();

    Class<? extends IValueStoreService> valueStoreService();

    Class<? extends INetworkMethods> networkService();

    Class<? extends IImageCacheService> imageCacheService();

    Class<? extends IMatchesService> matchesService();
}
