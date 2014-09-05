package ch.dreipol.android.blinq.ui.activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.View;

import com.activeandroid.util.Log;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.application.BlinqApplication;
import ch.dreipol.android.blinq.ui.fragments.BlinqFragment;
import ch.dreipol.android.blinq.ui.fragments.IHeaderConfigurationProvider;
import ch.dreipol.android.blinq.ui.fragments.ISettingsListListener;
import ch.dreipol.android.blinq.ui.fragments.ProfileSearchFragment;
import ch.dreipol.android.blinq.ui.fragments.MatchesListFragment;
import ch.dreipol.android.blinq.ui.fragments.MyProfileFragment;
import ch.dreipol.android.blinq.ui.fragments.MySettingsFragment;
import ch.dreipol.android.blinq.ui.fragments.SettingsListFragment;
import ch.dreipol.android.blinq.ui.fragments.webview.HelpWebViewFragment;
import ch.dreipol.android.blinq.ui.viewgroups.BlinqDrawerLayout;
import ch.dreipol.android.blinq.ui.viewgroups.DrawerPosition;
import ch.dreipol.android.blinq.util.exceptions.BlinqRuntimeException;

public class MainActivity extends BaseBlinqActivity implements ISettingsListListener {

    private BlinqDrawerLayout mLayout;
    private ProfileSearchFragment mProfileSearchFragment;
    private MatchesListFragment mRightFragment;
    private SettingsListFragment mLeftFragment;
    private BlinqFragment mCurrentCenterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        try {
////            ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo( PackageManager.GET_META_DATA);
//            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(
//                    getApplicationContext().getPackageName(),
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        mLayout = (BlinqDrawerLayout) findViewById(R.id.drawer_layout);

        mLayout.findViewById(R.id.blinq_header_left_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mLayout.getDrawerPosition().equals(DrawerPosition.RIGHT)){
                    setPosition(DrawerPosition.CENTER);
                }else{
                    setPosition(DrawerPosition.RIGHT);
                }

            }
        });
        mLayout.findViewById(R.id.blinq_header_right_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mLayout.getDrawerPosition().equals(DrawerPosition.LEFT)){
                    setPosition(DrawerPosition.CENTER);
                }else{
                    setPosition(DrawerPosition.LEFT);
                }
            }
        });


        setCenterFragment(ProfileSearchFragment.class);


        mLayout.setDrawerLayoutListener(new IDrawerLayoutListener() {

            @Override
            public void beginOrContinueMovement() {
                if (mRightFragment == null) {
                    mRightFragment = new MatchesListFragment();
                    getSupportFragmentManager().beginTransaction()
                            .add(mLayout.getRightContainer(), mRightFragment)
                            .commit();

                }

                if (mLeftFragment == null) {
                    mLeftFragment = new SettingsListFragment();

                    mLeftFragment.setSettingsListListener(MainActivity.this);

                    getSupportFragmentManager().beginTransaction()
                            .add(mLayout.getLeftContainer(), mLeftFragment)
                            .commit();

                }
            }

            @Override
            public void finishMovementOnPosition(DrawerPosition newPosition) {
                if(mCurrentCenterFragment!=null){
                    mCurrentCenterFragment.setPosition(newPosition);
                }
            }
        });


    }

    private void setCenterFragment(Class<? extends BlinqFragment> newCenterFragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();


        BlinqFragment newFragment;
        if (newCenterFragmentClass.isAssignableFrom(ProfileSearchFragment.class)) {
            if (mProfileSearchFragment == null) {
                mProfileSearchFragment = new ProfileSearchFragment();
            }
            newFragment = mProfileSearchFragment;
        } else {
            try {
                newFragment = newCenterFragmentClass.newInstance();
            } catch (InstantiationException e) {
                throw getBlinqRuntimeException();
            } catch (IllegalAccessException e) {
                throw getBlinqRuntimeException();
            }
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mCurrentCenterFragment != null) {
            fragmentTransaction.replace(mLayout.getCenterContainer(), newFragment);
        } else {
            fragmentTransaction
                    .add(mLayout.getCenterContainer(), newFragment);
        }

        fragmentTransaction.commit();

        mCurrentCenterFragment = newFragment;
        mCurrentCenterFragment.setGuiStatusObservable(mGuiLoadStatus);

        if (mCurrentCenterFragment instanceof IHeaderConfigurationProvider) {
            IHeaderConfigurationProvider headerProvider = (IHeaderConfigurationProvider) mCurrentCenterFragment;
            mLayout.updateHeaderConfiguration(headerProvider.getHeaderConfiguration());
        }

    }

    private BlinqRuntimeException getBlinqRuntimeException() {
        return new BlinqRuntimeException("Could not instantiate Fragment");
    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        UpdateManager.register(this, BlinqApplication.HOCKEY_APP_IDENTIFIER);
        CrashManager.register(this, BlinqApplication.HOCKEY_APP_IDENTIFIER);

    }

    @Override
    public void onBackPressed() {

        if(!mCurrentCenterFragment.equals(mProfileSearchFragment) && mLayout.getDrawerPosition().equals(DrawerPosition.CENTER)){
            setPosition(DrawerPosition.RIGHT);
        }else if (!mLayout.getDrawerPosition().equals(DrawerPosition.CENTER) && !mCurrentCenterFragment.equals(mProfileSearchFragment)) {
            setCenterFragment(ProfileSearchFragment.class);
            setPosition(DrawerPosition.CENTER);

        }else if (!mLayout.getDrawerPosition().equals(DrawerPosition.CENTER)) {
            setPosition(DrawerPosition.CENTER);

        } else {
            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public void helpTapped() {
        setCenterFragment(HelpWebViewFragment.class);
        setPosition(DrawerPosition.CENTER);
    }

    @Override
    public void settingsTapped() {
        setCenterFragment(MySettingsFragment.class);
        setPosition(DrawerPosition.CENTER);

    }

    @Override
    public void profileTapped() {
        setCenterFragment(MyProfileFragment.class);
        setPosition(DrawerPosition.CENTER);
    }


    @Override
    public void matchesTapped() {
        setPosition(DrawerPosition.LEFT);
    }

    @Override
    public void homeTapped() {
        setPosition(DrawerPosition.CENTER);
        setCenterFragment(ProfileSearchFragment.class);
    }

    private void setPosition(DrawerPosition drawerPosition) {

        mLayout.setDrawerPosition(drawerPosition);

    }

}