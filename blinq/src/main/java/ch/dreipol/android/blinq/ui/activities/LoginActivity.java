package ch.dreipol.android.blinq.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.widget.LoginButton;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import rx.Subscription;
import rx.functions.Action1;

public class LoginActivity extends BaseBlinqActivity {


    private Subscription mSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        IFacebookService facebookService = AppService.getInstance().getFacebookService();
        loginButton.setReadPermissions(facebookService.getPermissions());

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

}
