package ch.dreipol.android.blinq.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.ui.fragments.JoinBlinqFragment;
import ch.dreipol.android.dreiworks.ui.activities.ActivityTransitionType;

public class LaunchActivity extends BaseBlinqActivity implements JoinBlinqFragment.JoinBlinqFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);




        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();


        JoinBlinqFragment myFrag = new JoinBlinqFragment();
        Bundle args = new Bundle();
        args.putBoolean(JoinBlinqFragment.SHOW_MASQUERADE, true);
        myFrag.setArguments(args);
        fragTransaction.add(R.id.launch_container, myFrag);
        fragTransaction.commit();


    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Class<? extends Activity> startIntent;
                ActivityTransitionType transitionType = ActivityTransitionType.DEFAULT;

                if (AppService.getInstance().getFacebookService().hasFacebookSession()) {
                    startIntent = HomeActivity.class;
                } else {
                    startIntent = LoginActivity.class;
                    transitionType = ActivityTransitionType.CROSSFADE;
                }

                startActivity(startIntent, transitionType);
            }
        }, (long) 300.0f);

    }

    @Override
    public void showLoginScreen() {

    }

    @Override
    public void showNextScreen() {

    }
}


