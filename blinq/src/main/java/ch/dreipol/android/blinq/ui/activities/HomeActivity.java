package ch.dreipol.android.blinq.ui.activities;

import android.app.FragmentManager;
import android.os.Bundle;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.application.BlinqApplication;
import ch.dreipol.android.blinq.ui.fragments.MainFragment;
import ch.dreipol.android.blinq.ui.fragments.MatchesListFragment;
import ch.dreipol.android.blinq.ui.fragments.SettingsListFragment;
import ch.dreipol.android.blinq.ui.viewgroups.BlinqDrawerLayout;
import ch.dreipol.android.blinq.ui.viewgroups.DrawerSnap;

public class HomeActivity extends BaseBlinqActivity {

    private BlinqDrawerLayout layout;
    private MainFragment mainFragment;
    private MatchesListFragment rightFragment;
    private SettingsListFragment leftFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        layout = (BlinqDrawerLayout) findViewById(R.id.drawer_layout);


        FragmentManager fragmentManager = getFragmentManager();
        mainFragment = new MainFragment();
        mainFragment.setMainFragmentListener(new MainFragment.IMainFragmentListener() {
            @Override
            public void onSettingsClick() {
                layout.setDrawerPosition(DrawerSnap.RIGHT);
            }

            @Override
            public void onMatchesClick() {
                layout.setDrawerPosition(DrawerSnap.LEFT);


            }
        });
        fragmentManager.beginTransaction()
                .add(layout.getCenterContainer(), mainFragment)
                .commit();

        layout.setDrawerLayoutListener(new IDrawerLayoutListener() {

            @Override
            public void beginOrContinueMovement() {
                if (rightFragment == null) {
                    rightFragment = new MatchesListFragment();
                    getFragmentManager().beginTransaction()
                            .add(layout.getRightContainer(), rightFragment)
                            .commit();

                }

                if (leftFragment == null) {
                    leftFragment = new SettingsListFragment();
                    getFragmentManager().beginTransaction()
                            .add(layout.getLeftContainer(), leftFragment)
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
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
