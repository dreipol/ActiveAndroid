package ch.dreipol.android.blinq.services;

import android.content.Context;

/**
 * Created by phil on 22.03.14.
 */
public interface IServiceConfiguration {

    Context getContext();

    Class<? extends ILocationService> locationService();

    Class<? extends IFacebookService> sessionService();

    Class<? extends IValueStoreService> valueStoreService();

    Class<? extends INetworkService> networkService();
}
