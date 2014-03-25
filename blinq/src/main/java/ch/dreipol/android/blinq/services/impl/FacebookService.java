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

    public static final String FB_ID = "fbId";

    public enum FacebookServiceStatus {
        LOGGED_OUT, NO_FB_ID, LOGGED_IN
    }


    private BehaviorSubject<FacebookServiceStatus> mSessionStateSubject;

    @Override
    public void setup(AppService appService) {
        super.setup(appService);
        FacebookServiceStatus state;

        if (hasFacebookSession() && hasFaceookId()) {
            state = FacebookServiceStatus.LOGGED_IN;
        } else if (hasFacebookSession() && hasFaceookId()) {
            state = FacebookServiceStatus.NO_FB_ID;
        } else {
            state = FacebookServiceStatus.LOGGED_OUT;
        }

        mSessionStateSubject = BehaviorSubject.create(state);
        subscribeToSessionState().subscribe(new Action1<FacebookServiceStatus>() {
            @Override
            public void call(FacebookServiceStatus sessionState) {
                if (sessionState.equals(FacebookServiceStatus.NO_FB_ID)) {
                    getMe();
                }
            }
        });
    }

    private boolean hasFaceookId() {
        return getService().getValueStore().has(FB_ID);
    }

    public void updateSessionState(SessionState state) {
        FacebookServiceStatus facebookState = FacebookServiceStatus.LOGGED_OUT;
        if (state.isOpened() && !hasFaceookId()) {
            facebookState = FacebookServiceStatus.LOGGED_IN;
        } else if (state.isOpened() && hasFaceookId()) {
            facebookState = FacebookServiceStatus.NO_FB_ID;
        }else{
            getService().getValueStore().clear(FB_ID);
        }

        mSessionStateSubject.onNext(facebookState);
    }


    public Observable<FacebookServiceStatus> subscribeToSessionState() {
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

    public void getMe() {
        Bog.v(Bog.Category.FACEBOOK, "Retrieving Facebook User");
        if (hasFacebookSession()) {
            Request.newMeRequest(Session.getActiveSession(), new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    Bog.v(Bog.Category.FACEBOOK, "Received Facebook User Id: " + user.getId());
                    AppService.getInstance().getValueStore().put(FB_ID, user.getId());
                    updateSessionState(Session.getActiveSession().getState());
                }
            }).executeAsync();
        }

    }

    @Override
    public String getAccessToken() {
        return Session.getActiveSession().getAccessToken();
    }

}