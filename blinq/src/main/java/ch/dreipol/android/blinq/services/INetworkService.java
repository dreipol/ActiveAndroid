package ch.dreipol.android.blinq.services;

import java.util.Map;

import ch.dreipol.android.blinq.services.model.Profile;

/**
 * Created by phil on 26.03.14.
 */
public interface INetworkService extends IService{
    rx.Observable<Map<Long, Profile>> getSwarm(Map swarmBody);

    rx.Observable<Profile> getMe();
}
