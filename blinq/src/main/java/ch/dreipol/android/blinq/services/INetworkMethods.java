package ch.dreipol.android.blinq.services;

import ch.dreipol.android.blinq.services.model.Profile;

/**
 * Created by phil on 26.03.14.
 */
public interface INetworkMethods extends IService, ISwarmNetworkMethods, IMatchesNetworkMethods {

    rx.Observable<Profile> getMe();
}
