package ch.dreipol.android.blinq.ui.fragments;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;
import java.util.ListIterator;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IImageCacheService;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.Photo;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.services.model.SettingsProfile;
import ch.dreipol.android.blinq.ui.IProfileImageViewListener;
import ch.dreipol.android.blinq.ui.ProfileImageView;
import ch.dreipol.android.blinq.ui.ProfileImageViewType;
import ch.dreipol.android.blinq.ui.activities.FacebookPhotoPickerActivity;
import ch.dreipol.android.blinq.ui.headers.IHeaderViewConfiguration;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.blinq.util.StaticResources;
import ch.dreipol.android.dreiworks.ui.activities.ActivityTransitionType;
import ch.dreipol.android.dreiworks.ui.activities.BaseActivity;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

public class MyProfileFragment extends BlinqFragment implements IHeaderConfigurationProvider {


    private Subscription mMeSubscription;
    private SubscriptionList mImageSubscriptionList = new SubscriptionList();


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
                container.findViewById(R.id.loading_blocker).setVisibility(View.INVISIBLE);

                final RadioButton toggleMale = (RadioButton) container.findViewById(R.id.radio_male);

                final RadioButton toggleFemale = (RadioButton) container.findViewById(R.id.radio_female);

                if(profile.getSex().equals("m")){
                    toggleMale.setChecked(true);
                }else{
                    toggleFemale.setChecked(true);
                }

                toggleMale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                toggleFemale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


                TextView ageView = (TextView) container.findViewById(R.id.profile_age);
                TextView nameView = (TextView) container.findViewById(R.id.profile_name);
                ageView.setTextColor(bottomColor);
                nameView.setTextColor(bottomColor);

                Integer age = profile.getAge();

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
                ListIterator<Photo> it = profilePhotos.listIterator();

                ImageView imgView;
                ProfileImageView profileImageView;
                while (it.hasNext()) {
                    int position = it.nextIndex();
                    Photo photo = it.next();
                    if (position == 0) {
                        profileImageView = (ProfileImageView) container.findViewById(R.id.main_profile_view);
                        profileImageView.setType(ProfileImageViewType.BIG);
                    } else {

                        profileImageView = createProfileImageView(container, ProfileImageViewType.SMALL);

                        column.addView(profileImageView);
                        if (column.getChildCount() >= 2) {
                            column = new LinearLayout(container.getContext());
                            column.setOrientation(LinearLayout.VERTICAL);
                            column.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            imagesLayout.addView(column);

                        }
                    }
                    imgView = profileImageView.getImageView();
                    profileImageView.setOnClickListener(new ProfileImageViewListener(photo, position));

                    Subscription subscription = imageCacheService.displayPhoto(photo, imgView)
                            .observeOn(AndroidSchedulers.mainThread()).subscribe();
                    mImageSubscriptionList.add(subscription);
                }
                int photoCount = profilePhotos.size();
                if (photoCount < 5) {
                    profileImageView = createProfileImageView(container, ProfileImageViewType.ADD);
                    profileImageView.setOnClickListener(new ProfileImageViewListener(photoCount));
                    column.addView(profileImageView);
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

    private ProfileImageView createProfileImageView(View container, ProfileImageViewType type) {
        ProfileImageView profileImageView = new ProfileImageView(container.getContext());

        int size = StaticResources.convertDisplayPointsToPixel(container.getContext(), 70);
        profileImageView.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
        profileImageView.setType(type);
        return profileImageView;
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

            @Override
            public boolean hasIcon() {
                return true;
            }

            @Override
            public int getIconDrawable() {
                return R.drawable.icon_preview_standard;
            }

            @Override
            public void iconTapped() {

            }
        };
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_my_profile;
    }


    class ProfileImageViewListener implements IProfileImageViewListener {
        private Photo mPhoto = null;
        final private int mPosition;

        ProfileImageViewListener(Photo p, int position) {
            this(position);
            mPhoto = p;
        }

        ProfileImageViewListener(int position) {
            mPosition = position;
        }

        @Override
        public View.OnClickListener getEditListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BaseActivity activity = (BaseActivity) getActivity();
                    activity.getIntent().putExtra(BaseActivity.PHOTOSOURCE_POSITION, mPosition);
                    getGuiStatusObservable().subscribe(new Action1<LoadingInfo>() {
                        @Override
                        public void call(LoadingInfo o) {
                            View blocker = getView().findViewById(R.id.loading_blocker);
                            switch (o.getState()) {
                                case LOADING:
                                    blocker.setVisibility(View.VISIBLE);
                                    break;
                                case LOADED:
                                case CANCELED:
                                case ERROR:
                                    blocker.setVisibility(View.INVISIBLE);
                                    break;
                            }
                        }
                    });
                    activity.startActivityForResult(FacebookPhotoPickerActivity.class, ActivityTransitionType.DEFAULT, BaseActivity.FACEBOOK_PHOTOPICKER_REQUEST);
                }
            };
        }

        @Override
        public View.OnClickListener getDeleteListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPhoto != null) {
                        AppService.getInstance().getAccountService().removePhoto(mPhoto);
                    }
                }
            };
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

}
