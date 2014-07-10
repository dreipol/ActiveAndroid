package ch.dreipol.android.blinq.services;

import java.util.Map;

/**
 * Created by phil on 26.03.14.
 */
public interface INetworkService extends IService{
    rx.Observable getSwarm(Map swarmBody);

    rx.Observable<Profile> getMe();
}
