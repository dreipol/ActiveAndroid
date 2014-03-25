package ch.dreipol.android.blinq.services.impl;

import com.facebook.Session;
import com.facebook.SessionState;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IFacebookService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by phil on 24.03.14.
 */
public class FacebookService extends BaseService implements IFacebookService {


    private BehaviorSubject<SessionState> mSessionStateSubject;

    @Override
    public void setup(AppService appService) {
        super.setup(appService);
        SessionState state;
        if (hasFacebookSession()) {
            state = Session.getActiveSession().getState();
        } else {
            state = SessionState.CLOSED;
        }

        mSessionStateSubject = BehaviorSubject.create(state);

    }

    public void updateSessionState(SessionState state) {
        mSessionStateSubject.onNext(state);
    }


    public Observable<SessionState> subscribeToSessionState() {
        return mSessionStateSubject.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public boolean hasFacebookSession() {
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getAccessToken() {
        return Session.getActiveSession().getAccessToken();
    }

}