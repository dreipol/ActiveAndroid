package ch.dreipol.android.blinq.services.impl;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import java.util.Arrays;
import java.util.List;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.util.Bog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
        subscribeToSessionState().subscribe(new Action1<SessionState>() {
            @Override
            public void call(SessionState sessionState) {
                if(sessionState.isOpened()){
                    getMe();
                }
            }
        });
    }

    public void updateSessionState(SessionState state) {
        mSessionStateSubject.onNext(state);
    }


    public Observable<SessionState> subscribeToSessionState() {
        return mSessionStateSubject.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public List<String> getPermissions() {
        return Arrays.asList(
                "user_about_me",
                "user_birthday",
                "user_likes",
                "user_photos",
                "user_status",
                "user_education_history",
                "email");
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

    public void getMe(){
        Bog.v(Bog.Category.FACEBOOK, "Retrieving Facebook User");
        Request.newMeRequest(Session.getActiveSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                Bog.v(Bog.Category.FACEBOOK, "Received Facebook User Id: " + user.getId());
                AppService.getInstance().getValueStore().put("fbId", user.getId());
            }
        }).executeAsync();
    }

    @Override
    public String getAccessToken() {
        return Session.getActiveSession().getAccessToken();
    }

}