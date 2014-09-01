package ch.dreipol.android.blinq.services;

import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.services.model.SettingsProfile;
import ch.dreipol.android.blinq.services.network.UploadProfile;
import rx.Observable;

/**
 * Created by phil on 26.03.14.
 */
public interface INetworkMethods extends IService, ISwarmNetworkMethods, IMatchesNetworkMethods {

    void getMe();

    Observable<SettingsProfile> signup();

    void update(UploadProfile profile);
}
