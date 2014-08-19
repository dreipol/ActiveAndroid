package ch.dreipol.android.blinq.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.ui.fragments.JoinBlinqFragment;
import ch.dreipol.android.blinq.ui.fragments.PrivacyDisclaimer;
import rx.Subscription;
import rx.functions.Action1;

public class LoginActivity extends BaseBlinqActivity implements JoinBlinqFragment.JoinBlinqFragmentListener {

    private Subscription mSubscribe;


//    private Subscription mSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        JoinBlinqFragment myFrag = new JoinBlinqFragment();
        transaction.setCustomAnimations(0, 0, 0, 0);
        transaction.add(R.id.login_container, myFrag);
        transaction.commit();

        IFacebookService facebookService = AppService.getInstance().getFacebookService();
        mSubscribe = facebookService.subscribeToSessionState().subscribe(new Action1<FacebookService.FacebookServiceInfo>() {
            @Override
            public void call(FacebookService.FacebookServiceInfo info) {
                if (info.status.equals(FacebookService.FacebookServiceStatus.LOGGED_IN)) {
                    Intent startIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(startIntent);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscribe.unsubscribe();
    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return true;
    }

    @Override
    public void showLoginScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_to_top, R.anim.none, R.anim.none, R.anim.top_to_bottom);
        PrivacyDisclaimer privacyDisclaimer = new PrivacyDisclaimer();
        transaction.add(R.id.login_container, privacyDisclaimer);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void showNextScreen() {

    }
}
