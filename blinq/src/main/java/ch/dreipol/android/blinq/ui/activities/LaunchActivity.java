package ch.dreipol.android.blinq.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;

public class LaunchActivity extends BaseBlinqActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

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

                Intent startIntent;

                if (AppService.getInstance().getSessionService().hasFacebookSession()) {
                    startIntent = new Intent(getApplicationContext(), HomeActivity.class);
                } else {
                    startIntent = new Intent(getApplicationContext(), LoginActivity.class);
                }

                startActivity(startIntent);
            }
        }, (long) 300.0f);

    }
}


