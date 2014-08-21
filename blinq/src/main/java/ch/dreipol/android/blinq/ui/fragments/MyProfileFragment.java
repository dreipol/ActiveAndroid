package ch.dreipol.android.blinq.ui.fragments;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.model.Profile;
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

                int bottomColor = Color.parseColor(profile.getColorBottom());

                View container = loadingInfo.getViewContainer();

                ImageView imageView = (ImageView) container.findViewById(R.id.main_image);
                AppService.getInstance().getImageCacheService().displayPhoto(profile.getPhotos().get(0), imageView);

                TextView ageView = (TextView) container.findViewById(R.id.profile_age);
                TextView nameView = (TextView) container.findViewById(R.id.profile_name);
                ageView.setTextColor(bottomColor);
                nameView.setTextColor(bottomColor);

                Integer age = profile.getAge();

                if (age == null) {
//                    TODO: Phil use birthday
                    age = 99;
                }

                ageView.setText(age.toString());
                nameView.setText(profile.getFirstName());


                LinearLayout profileOverviewView = (LinearLayout) container.findViewById(R.id.profile_overview);
                GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(profile.getColorTop()), bottomColor});
                g.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                profileOverviewView.setBackgroundDrawable(g);

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
        mUIState = null;
        mProfileObservable = null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_my_profile, container, false);

        final ToggleButton toggleMale = (ToggleButton) result.findViewById(R.id.toggle_male);

        final ToggleButton toggleFemale = (ToggleButton) result.findViewById(R.id.toggle_female);

        toggleMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFemale.setChecked(false);
            }
        });

        toggleFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMale.setChecked(false);
            }
        });


        mUIState.onNext(result);
        return result;
    }


}
