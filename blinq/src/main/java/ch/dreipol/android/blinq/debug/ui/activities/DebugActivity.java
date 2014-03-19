package ch.dreipol.android.blinq.debug.ui.activities;

import android.os.Bundle;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.ui.activities.BlinqBaseActivity;

public class DebugActivity extends BlinqBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
    }
}
