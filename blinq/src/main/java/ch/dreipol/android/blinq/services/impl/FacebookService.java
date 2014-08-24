package ch.dreipol.android.blinq.services.impl;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.services.model.ILoadable;
import ch.dreipol.android.blinq.services.model.facebook.FacebookAlbum;
import ch.dreipol.android.blinq.services.model.facebook.FacebookPhoto;
import ch.dreipol.android.blinq.services.model.facebook.FacebookPhotoSource;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.dreiworks.serialization.gson.GsonHelper;
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
    private String FB_NAME = "fbName";

    public class FacebookUser {
        public String name;
        public String id;

        public FacebookUser() {
            this.name = "";
            this.id = "";
        }
    }


    public class FacebookServiceInfo {
        public FacebookServiceStatus status;
        public FacebookUser user;

        public boolean hasUser() {
            return user != null;
        }

        public boolean isLoggedIn() {
            return status == FacebookServiceStatus.LOGGED_IN;
        }
    }

    public enum FacebookServiceStatus {
        LOGGED_OUT(0), NO_FB_ID(1), LOGGED_IN(2);

        private final int value;

        private FacebookServiceStatus(int val) {
            this.value = val;
        }

        public int getValue() {
            return this.value;
        }
    }


    private BehaviorSubject<FacebookServiceInfo> mSessionStateSubject;

    @Override
    public void setup(AppService appService) {
        super.setup(appService);
        FacebookServiceInfo info = new FacebookServiceInfo();
        info.user = getFBUser();
        if (hasFacebookSession() && hasFacebookId()) {
            info.status = FacebookServiceStatus.LOGGED_IN;
        } else if (hasFacebookSession() && hasFacebookId()) {
            info.status = FacebookServiceStatus.NO_FB_ID;
        } else {
            info.status = FacebookServiceStatus.LOGGED_OUT;
        }

        mSessionStateSubject = BehaviorSubject.create(info);
        subscribeToSessionState().subscribe(new Action1<FacebookServiceInfo>() {
            @Override
            public void call(FacebookServiceInfo serviceInfo) {
                if (serviceInfo.status.equals(FacebookServiceStatus.NO_FB_ID)) {
                    getMe();
                }
            }
        });
    }

    private boolean hasFacebookId() {
        return getService().getValueStore().has(FB_ID);
    }

    public void updateSessionState(SessionState state) {

        FacebookServiceInfo info = new FacebookServiceInfo();
        info.user = getFBUser();
        if (state.isOpened() && hasFacebookId()) {
            info.status = FacebookServiceStatus.LOGGED_IN;
        } else if (state.isOpened() && !hasFacebookId()) {
            info.status = FacebookServiceStatus.NO_FB_ID;
        } else {
            info.status = FacebookServiceStatus.LOGGED_OUT;
            getService().getValueStore().clear(FB_ID);
            getService().getValueStore().clear(FB_NAME);

        }

        mSessionStateSubject.onNext(info);
    }

    private FacebookUser getFBUser() {
        FacebookUser user = new FacebookUser();
        if (hasFacebookSession() && hasFacebookId()) {
            user.id = getFacebookId();
            user.name = getService().getValueStore().get(FB_NAME);
        }
        return user;
    }


    public Observable<FacebookServiceInfo> subscribeToSessionState() {
        return mSessionStateSubject.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
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
    public String getFacebookId() {
        return getService().getValueStore().get(FB_ID);
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
                    AppService.getInstance().getValueStore().put(FB_NAME, user.getFirstName());
                    updateSessionState(Session.getActiveSession().getState());
                }
            }).executeAsync();
        }
    }

    public BehaviorSubject<FacebookAlbumListResponse> getAlbums() {

        final BehaviorSubject<FacebookAlbumListResponse> subject = BehaviorSubject.create();

        executeRequest(subject, new ExecutorAdapter(subject) {
            @Override
            public void success(Response response) {
                subject.onNext(FacebookAlbumListResponse.createFromGraph(response.getGraphObject()));
            }

            @Override
            public String getEndPoint() {
                return "/me/albums";
            }
        });

        return subject;

    }

    private void executeRequest(BehaviorSubject subject, Executor executor) {
        if (hasFacebookSession()) {
            executor.execute();
        } else {
            subject.onError(new Throwable("No Facebook Session. Please Log In."));
        }
    }


    public BehaviorSubject<FacebookAlbumResponse> getPhotosFromAlbum(final String albumId) {

        final BehaviorSubject<FacebookAlbumResponse> subject = BehaviorSubject.create();

        executeRequest(subject, new ExecutorAdapter(subject) {
            @Override
            public void success(Response response) {
                subject.onNext(FacebookAlbumResponse.createFromGraph(response.getGraphObject()));
            }

            @Override
            public String getEndPoint() {
                return String.format("/%s/photos", albumId);
            }
        });

        return subject;
    }

    public BehaviorSubject<FacebookPhoto> getPhotoForId(final String photoId) {

        final BehaviorSubject<FacebookPhoto> subject = BehaviorSubject.create();
        executeRequest(subject, new ExecutorAdapter(subject) {

            @Override
            public void success(Response response) {
                subject.onNext(FacebookPhoto.createFromGraph(response.getGraphObject()));
            }


            @Override
            public String getEndPoint() {
                return String.format("/%s", photoId);
            }
        });


        return subject;
    }


    @Override
    public String getAccessToken() {
        return Session.getActiveSession().getAccessToken();
    }

    public static class FacebookAlbumListResponse {
        public Collection<FacebookAlbum> mData;

        public static FacebookAlbumListResponse createFromGraph(GraphObject response) {
            Gson gson = GsonHelper.getFacebookGSONDeserializationBuilder().create();
            return gson.fromJson(response.getInnerJSONObject().toString(), FacebookAlbumListResponse.class);
        }
    }

    public static class FacebookAlbumResponse implements ILoadable{
        public Collection<FacebookPhoto> mData;

        public static FacebookAlbumResponse createFromGraph(GraphObject response) {
            Gson gson = GsonHelper.getFacebookGSONDeserializationBuilder().create();
            return gson.fromJson(response.getInnerJSONObject().toString(), FacebookAlbumResponse.class);
        }
    }

    private interface Executor {
        public void execute();

        void success(Response response);

        String getEndPoint();
    }


    private abstract class ExecutorAdapter implements Executor {


        private BehaviorSubject mSubject;

        public BehaviorSubject getSubject() {
            return mSubject;
        }

        protected ExecutorAdapter(BehaviorSubject subject) {

            mSubject = subject;
        }

        public void checkResponse(Response response) {
            FacebookRequestError responseError = response.getError();
            if (responseError != null) {
                Bog.e(Bog.Category.FACEBOOK, responseError.getErrorMessage());
                BehaviorSubject subject = getSubject();
                if(subject!=null){
                    subject.onError(new Throwable(responseError.getErrorMessage()));
                }

            } else {
                success(response);
            }
        }

        @Override
        public void execute() {
            buildRequest().executeAsync();
        }

        private Request buildRequest() {
            return new Request(
                    Session.getActiveSession(),
                    getEndPoint(),
                    null,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            checkResponse(response);
                        }
                    }
            );
        }


    }
}