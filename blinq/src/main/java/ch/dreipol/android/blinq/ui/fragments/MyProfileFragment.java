package ch.dreipol.android.blinq.ui.fragments;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.common.io.LineReader;

import java.util.List;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IImageCacheService;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.Photo;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.services.model.SettingsProfile;
import ch.dreipol.android.blinq.ui.ProfileImageView;
import ch.dreipol.android.blinq.ui.ProfileImageViewType;
import ch.dreipol.android.blinq.ui.headers.IHeaderViewConfiguration;
import ch.dreipol.android.blinq.ui.layout.FlowLayout;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.blinq.util.StaticResources;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

public class MyProfileFragment extends BlinqFragment implements IHeaderConfigurationProvider {


    private Subscription mMeSubscription;
    private SubscriptionList mImageSubscriptionList = new SubscriptionList();

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


                final ToggleButton toggleMale = (ToggleButton) container.findViewById(R.id.toggle_male);

                final ToggleButton toggleFemale = (ToggleButton) container.findViewById(R.id.toggle_female);

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

                ProfileImageView mainProfileImageView = (ProfileImageView) container.findViewById(R.id.main_profile_view);
                mainProfileImageView.setType(ProfileImageViewType.BIG);
                ImageView imageView = mainProfileImageView.getImageView();


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

                RelativeLayout profileOverviewView = (RelativeLayout) container.findViewById(R.id.profile_background);
                GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(profile.getColorTop()), bottomColor});
                g.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                profileOverviewView.setBackgroundDrawable(g);

                LinearLayout imagesLayout = (LinearLayout) container.findViewById(R.id.small_images);
                imagesLayout.removeAllViews();

                IImageCacheService imageCacheService = AppService.getInstance().getImageCacheService();

                LinearLayout column = new LinearLayout(container.getContext());
                column.setOrientation(LinearLayout.VERTICAL);
                column.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imagesLayout.addView(column);

                List<Photo> profilePhotos = profile.getPhotos();

                for (Photo photo : profilePhotos) {
                    ImageView imgView = imageView;
                    if (profilePhotos.indexOf(photo) != 0) {

                        ProfileImageView profileImageView = new ProfileImageView(container.getContext());

                        int size = StaticResources.convertDisplayPointsToPixel(container.getContext(), 60);
                        profileImageView.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
                        profileImageView.setType(ProfileImageViewType.SMALL);
                        imgView = profileImageView.getImageView();
                        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        column.addView(profileImageView);
                        if (column.getChildCount() == 2) {
                            column = new LinearLayout(container.getContext());
                            column.setOrientation(LinearLayout.VERTICAL);
                            column.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            imagesLayout.addView(column);

                        }
                    }


                    Subscription subscription = imageCacheService.displayPhoto(photo, imgView)
                            .observeOn(AndroidSchedulers.mainThread()).subscribe();
                    mImageSubscriptionList.add(subscription);
                }


            }
        });

        mMeSubscription = AppService.getInstance().getAccountService().getMe()
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
        mMeSubscription.unsubscribe();
        mImageSubscriptionList.unsubscribe();
    }

    @Override
    public IHeaderViewConfiguration getHeaderConfiguration() {
        return new IHeaderViewConfiguration() {
            @Override
            public boolean showTitle() {
                return true;
            }

            @Override
            public String getTitle() {
                return "Profile";
            }
        };
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_my_profile;
    }


}
