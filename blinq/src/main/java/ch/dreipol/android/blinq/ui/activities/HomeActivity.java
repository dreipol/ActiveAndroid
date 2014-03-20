package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import ch.dreipol.android.blinq.BlinqApplication;
import ch.dreipol.android.blinq.R;

public class HomeActivity extends BlinqBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
