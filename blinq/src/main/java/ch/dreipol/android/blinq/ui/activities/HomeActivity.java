package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.application.BlinqApplication;
import ch.dreipol.android.blinq.ui.fragments.IHeaderConfigurationProvider;
import ch.dreipol.android.blinq.ui.fragments.ISettingsListListener;
import ch.dreipol.android.blinq.ui.fragments.MainFragment;
import ch.dreipol.android.blinq.ui.fragments.MatchesListFragment;
import ch.dreipol.android.blinq.ui.fragments.MySettingsFragment;
import ch.dreipol.android.blinq.ui.fragments.SettingsListFragment;
import ch.dreipol.android.blinq.ui.fragments.webview.HelpWebViewFragment;
import ch.dreipol.android.blinq.ui.viewgroups.BlinqDrawerLayout;
import ch.dreipol.android.blinq.ui.viewgroups.DrawerPosition;
import ch.dreipol.android.blinq.util.exceptions.BlinqRuntimeException;

public class HomeActivity extends BaseBlinqActivity implements ISettingsListListener {

    private BlinqDrawerLayout mLayout;
    private MainFragment mMainFragment;
    private MatchesListFragment mRightFragment;
    private SettingsListFragment mLeftFragment;
    private Fragment mCurrentCenterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mLayout = (BlinqDrawerLayout) findViewById(R.id.drawer_layout);

//
//        mLayout.findViewById(R.id.blinq_header_left_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mLayout.setDrawerPosition(DrawerPosition.RIGHT);
//
//            }
//        });
//        mLayout.findViewById(R.id.blinq_header_right_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mLayout.setDrawerPosition(DrawerPosition.LEFT);
//            }
//        });
//


        setCenterFragment(MainFragment.class);


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

                    mLeftFragment.setSettingsListListener(HomeActivity.this);

                    getSupportFragmentManager().beginTransaction()
                            .add(mLayout.getLeftContainer(), mLeftFragment)
                            .commit();

                }
            }
        });


    }

    private void setCenterFragment(Class<? extends Fragment> newCenterFragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment newFragment;
        if (newCenterFragmentClass.isInstance(MainFragment.class)) {
            if (mMainFragment == null) {
                mMainFragment = new MainFragment();
            }
            newFragment = mMainFragment;
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

        if(mCurrentCenterFragment instanceof IHeaderConfigurationProvider){
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

        if (!mLayout.getDrawerPosition().equals(DrawerPosition.CENTER)) {
            mLayout.setDrawerPosition(DrawerPosition.CENTER);
        } else {
            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public void helpTapped() {
        mLayout.setDrawerPosition(DrawerPosition.CENTER);
        setCenterFragment(HelpWebViewFragment.class);
    }

    @Override
    public void settingsTapped() {
        mLayout.setDrawerPosition(DrawerPosition.CENTER);
        setCenterFragment(MySettingsFragment.class);

    }

    @Override
    public void profileTapped() {
        mLayout.setDrawerPosition(DrawerPosition.CENTER);
    }

    @Override
    public void matchesTapped() {
        mLayout.setDrawerPosition(DrawerPosition.LEFT);
    }

    @Override
    public void homeTapped() {
        mLayout.setDrawerPosition(DrawerPosition.CENTER);
        setCenterFragment(MainFragment.class);
    }
}
