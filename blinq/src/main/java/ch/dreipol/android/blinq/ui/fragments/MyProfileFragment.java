package ch.dreipol.android.blinq.ui.fragments;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IImageCacheService;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.Photo;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.services.model.SettingsProfile;
import ch.dreipol.android.blinq.ui.layout.FlowLayout;
import ch.dreipol.android.blinq.util.Bog;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MyProfileFragment extends BlinqFragment {


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


        mLoadingSubscription.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<LoadingInfo>() {
            @Override
            public void call(LoadingInfo loadingInfo) {

                Profile profile = (Profile) loadingInfo.getData();

                int bottomColor = Color.parseColor(profile.getColorBottom());

                View container = loadingInfo.getViewContainer();

                ImageView imageView = (ImageView) container.findViewById(R.id.main_image);


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

                FlowLayout flowLayout = (FlowLayout) container.findViewById(R.id.flow_layout);
                flowLayout.removeAllViews();

                IImageCacheService imageCacheService = AppService.getInstance().getImageCacheService();

                List<Photo> profilePhotos = profile.getPhotos();
                for (Photo photo : profilePhotos) {
                    Bog.d(Bog.Category.UI, "loading photo...");

                    if (profilePhotos.indexOf(photo) == 0) {
                        imageCacheService.displayPhoto(photo, imageView);
                        Bog.d(Bog.Category.UI, "...main");
                    } else {
                        ImageView imgView = new ImageView(container.getContext());
                        flowLayout.addView(imgView);
                        imageCacheService.displayPhoto(photo, imageView);
                        Bog.d(Bog.Category.UI, "...small");
                    }
                }


            }
        });

        AppService.getInstance().getNetworkService().getMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SettingsProfile>() {
                               @Override
                               public void call(SettingsProfile profile) {
                                   LoadingInfo<SettingsProfile> loadingInfo = new LoadingInfo<SettingsProfile>(LoadingState.LOADED);
                                   loadingInfo.setData(profile);
                                   mDataSubject.onNext(loadingInfo);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   mDataSubject.onNext(new LoadingInfo<Profile>(LoadingState.ERROR));
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
        mLoadingSubscription.unsubscribeOn(Schedulers.io());

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_my_profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View result = super.onCreateView(inflater, container, savedInstanceState);

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


        return result;
    }


}
