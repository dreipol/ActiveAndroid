package ch.dreipol.android.blinq.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.Session;
import com.facebook.SessionState;

import ch.dreipol.android.blinq.R;

public class LoginActivity extends BaseBlinqActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    protected void onSessionStateChange(Session session, SessionState state, Exception exception) {
        super.onSessionStateChange(session, state, exception);
        if(state.isOpened()){
            Intent startIntent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(startIntent);
        }
    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return true;
    }

}
