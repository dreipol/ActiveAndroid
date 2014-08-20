package ch.dreipol.android.blinq.ui.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.ui.ProfileOverviewView;
import ch.dreipol.android.blinq.util.Bog;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class MyProfileFragment extends Fragment {


    private BehaviorSubject<View> mUIState;
    private BehaviorSubject<LoadingInfo> mProfileObservable;
    private Subscription mReadySubscription;

    private class LoadingInfo {

        private Profile mProfile;
        private View mViewContainer;
        private final LoadingState mState;

        private LoadingInfo(LoadingState mState) {
            this.mState = mState;
        }

        public Profile getProfile() {
            return mProfile;
        }

        public void setProfile(Profile mProfile) {
            this.mProfile = mProfile;
        }

        public LoadingState getState() {
            return mState;
        }

        public View getViewContainer() {
            return mViewContainer;
        }

        public void setViewContainer(View viewContainer) {
            mViewContainer = viewContainer;
        }
    }

    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUIState = BehaviorSubject.create();
        mProfileObservable = BehaviorSubject.create();

        mReadySubscription = Observable.zip(mUIState, mProfileObservable, new Func2<View, LoadingInfo, LoadingInfo>() {
            @Override
            public LoadingInfo call(View view, LoadingInfo loadingInfo) {
                loadingInfo.setViewContainer(view);
                return loadingInfo;
            }
        }).subscribe(new Action1<LoadingInfo>() {
            @Override
            public void call(LoadingInfo loadingInfo) {
                Profile profile = loadingInfo.getProfile();

                ProfileOverviewView profileOverviewView = (ProfileOverviewView) loadingInfo.getViewContainer().findViewById(R.id.profile_overview);

                profileOverviewView.setGradient(Color.parseColor(profile.getColor_top()), Color.parseColor(profile.getColor_bottom()));
            }
        });

        AppService.getInstance().getNetworkService().getMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Profile>() {
                               @Override
                               public void call(Profile profile) {
                                   LoadingInfo loadingInfo = new LoadingInfo(LoadingState.LOADED);
                                   loadingInfo.setProfile(profile);
                                   mProfileObservable.onNext(loadingInfo);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   mProfileObservable.onNext(new LoadingInfo(LoadingState.ERROR));
                               }
                           },
                        new Action0() {
                            @Override
                            public void call() {
                                Bog.v(Bog.Category.NETWORKING, "Finished");
                            }
                        }
                );

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mReadySubscription.unsubscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_my_profile, container, false);
        mUIState.onNext(result);
        return result;
    }


}
