package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.ui.fragments.JoinBlinqFragment;
import ch.dreipol.android.blinq.ui.fragments.PrivacyDisclaimer;

public class LoginActivity extends BaseBlinqActivity implements JoinBlinqFragment.JoinBlinqFragmentListener{


//    private Subscription mSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);



        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();


        JoinBlinqFragment myFrag = new JoinBlinqFragment();
        fragTransaction.add(R.id.login_container, myFrag);
        fragTransaction.commit();

//        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
//        IFacebookService facebookService = AppService.getInstance().getFacebookService();
//        loginButton.setReadPermissions(facebookService.getPermissions());
//
//        mSubscribe = facebookService.subscribeToSessionState().subscribe(new Action1<FacebookService.FacebookServiceInfo>() {
//            @Override
//            public void call(FacebookService.FacebookServiceInfo info) {
//                if (info.status.equals(FacebookService.FacebookServiceStatus.LOGGED_IN)) {
//                    Intent startIntent = new Intent(getApplicationContext(), HomeActivity.class);
//                    startActivity(startIntent);
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mSubscribe.unsubscribe();
    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return true;
    }

    @Override
    public void showLoginScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        /transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        PrivacyDisclaimer newCustomFragment = new PrivacyDisclaimer();
        transaction.replace(R.id.login_container, newCustomFragment );
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void showNextScreen() {

    }
}
