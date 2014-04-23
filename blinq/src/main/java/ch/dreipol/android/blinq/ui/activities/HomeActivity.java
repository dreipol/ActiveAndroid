package ch.dreipol.android.blinq.ui.activities;

import android.app.FragmentManager;
import android.os.Bundle;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.application.BlinqApplication;
import ch.dreipol.android.blinq.ui.fragments.ISettingsListListener;
import ch.dreipol.android.blinq.ui.fragments.MainFragment;
import ch.dreipol.android.blinq.ui.fragments.MatchesListFragment;
import ch.dreipol.android.blinq.ui.fragments.SettingsListFragment;
import ch.dreipol.android.blinq.ui.viewgroups.BlinqDrawerLayout;
import ch.dreipol.android.blinq.ui.viewgroups.DrawerPosition;

public class HomeActivity extends BaseBlinqActivity implements ISettingsListListener{

    private BlinqDrawerLayout mLayout;
    private MainFragment mMainFragment;
    private MatchesListFragment mRightFragment;
    private SettingsListFragment mLeftFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mLayout = (BlinqDrawerLayout) findViewById(R.id.drawer_layout);


        FragmentManager fragmentManager = getFragmentManager();
        mMainFragment = new MainFragment();
        mMainFragment.setMainFragmentListener(new MainFragment.IMainFragmentListener() {
            @Override
            public void onSettingsClick() {
                mLayout.setDrawerPosition(DrawerPosition.RIGHT);
            }

            @Override
            public void onMatchesClick() {
                mLayout.setDrawerPosition(DrawerPosition.LEFT);


            }
        });
        fragmentManager.beginTransaction()
                .add(mLayout.getCenterContainer(), mMainFragment)
                .commit();

        mLayout.setDrawerLayoutListener(new IDrawerLayoutListener() {

            @Override
            public void beginOrContinueMovement() {
                if (mRightFragment == null) {
                    mRightFragment = new MatchesListFragment();
                    getFragmentManager().beginTransaction()
                            .add(mLayout.getRightContainer(), mRightFragment)
                            .commit();

                }

                if (mLeftFragment == null) {
                    mLeftFragment = new SettingsListFragment(HomeActivity.this);

                    getFragmentManager().beginTransaction()
                            .add(mLayout.getLeftContainer(), mLeftFragment)
                            .commit();

                }
            }
        });








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

        if(!mLayout.getDrawerPosition().equals(DrawerPosition.CENTER)){
            mLayout.setDrawerPosition(DrawerPosition.CENTER);
        }else{
            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public void helpTapped() {

    }

    @Override
    public void settingsTapped() {

    }

    @Override
    public void profileTapped() {

    }

    @Override
    public void matchesTapped() {
        mLayout.setDrawerPosition(DrawerPosition.LEFT);
    }

    @Override
    public void homeTapped() {
        mLayout.setDrawerPosition(DrawerPosition.CENTER);
    }
}
