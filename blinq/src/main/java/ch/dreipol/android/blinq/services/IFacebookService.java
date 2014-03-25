package ch.dreipol.android.blinq.services;

import com.facebook.SessionState;

import rx.Observable;

/**
 * Created by phil on 24.03.14.
 */
public interface IFacebookService extends IService{
    boolean hasFacebookSession();

    String getAccessToken();

    void updateSessionState(SessionState state);

    Observable<SessionState> subscribeToSessionState();
}
