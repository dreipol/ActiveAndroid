package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.dreiworks.ui.activities.ActivityTransitionType;

public class DebugActivity extends BaseBlinqActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        findButtonWithId(R.id.debug_system_information).setOnClickListener(createActivityClickListener(SystemInformationActivity.class, ActivityTransitionType.DEFAULT));
        findButtonWithId(R.id.debug_network_services).setOnClickListener(createActivityClickListener(NetworkDebugActivity.class, ActivityTransitionType.DEFAULT));
        findButtonWithId(R.id.debug_facebook_button).setOnClickListener(createActivityClickListener(FacebookDebugActivity.class, ActivityTransitionType.DEFAULT));

    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }

}
