package ch.dreipol.android.blinq.services;

import com.facebook.SessionState;

import java.util.List;

import ch.dreipol.android.blinq.services.impl.FacebookService;
import rx.Observable;

/**
 * Created by phil on 24.03.14.
 */
public interface IFacebookService extends IService{
    boolean hasFacebookSession();

    String getAccessToken();

    void updateSessionState(SessionState state);

    Observable<FacebookService.FacebookServiceInfo> subscribeToSessionState();

    List<String> getPermissions();

    String getFacebookId();

    public rx.subjects.BehaviorSubject<FacebookService.FacebookAlbumListResponse> getAlbums();
}
