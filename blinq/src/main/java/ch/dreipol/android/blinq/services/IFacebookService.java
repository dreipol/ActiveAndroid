package ch.dreipol.android.blinq.services;

import com.facebook.SessionState;

import java.util.List;

import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.services.model.facebook.FacebookPhoto;
import ch.dreipol.android.blinq.services.model.facebook.FacebookPhotoSource;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by phil on 24.03.14.
 */
public interface IFacebookService extends IService {
    boolean hasFacebookSession();

    String getAccessToken();

    void updateSessionState(SessionState state);

    Observable<FacebookService.FacebookServiceInfo> subscribeToSessionState();

    List<String> getPermissions();

    String getFacebookId();

    public BehaviorSubject<FacebookService.FacebookAlbumListResponse> getAlbums();

    public BehaviorSubject<FacebookService.FacebookAlbumResponse> getPhotosFromAlbum(final String albumId);

    public BehaviorSubject<FacebookPhoto> getPhotoForId(final String photoId);

    public BehaviorSubject<FacebookService.FacebookAlbumResponse> getPhotosOfMe();
}
