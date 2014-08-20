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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MyProfileFragment extends Fragment {


    private Profile mProfile;

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
        Bog.v(Bog.Category.NETWORKING, "Start");
        AppService.getInstance().getNetworkService().getMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Profile>() {
                               @Override
                               public void call(Profile profile) {
                                   setProfile(profile);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   Bog.v(Bog.Category.NETWORKING, "Finished");
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

    private void setProfile(Profile profile) {

        this.mProfile = profile;
        updateUI();
    }

    private void updateUI() {
        if(this.mProfile !=null && getView() !=null){
            ProfileOverviewView profileOverviewView = (ProfileOverviewView) getView().findViewById(R.id.profile_overview);

            profileOverviewView.setGradient(Color.parseColor(mProfile.getColor_top()),Color.parseColor(mProfile.getColor_bottom()) );

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        updateUI();;
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }


}
