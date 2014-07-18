package ch.dreipol.android.blinq.services;

import java.util.Map;

import ch.dreipol.android.blinq.services.model.Profile;
import rx.Observable;

/**
 * Created by melbic on 18/07/14.
 */
public interface ISwarmNetworkService {
    public Observable<Map<Long, Profile>> getSwarm(Map swarmBody);

    public Observable<Profile> hi(Profile other);

    public void bye(Profile other);
}
