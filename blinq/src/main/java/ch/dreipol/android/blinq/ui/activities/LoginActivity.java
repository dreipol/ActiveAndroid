package ch.dreipol.android.blinq.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.SessionState;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import rx.functions.Action1;

public class LoginActivity extends BaseBlinqActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppService.getInstance().getFacebookService().subscribeToSessionState().subscribe(new Action1<SessionState>() {
            @Override
            public void call(SessionState sessionState) {
                if(sessionState.isOpened()){
                    Intent startIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(startIntent);
                }
            }
        });
    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return true;
    }

}
