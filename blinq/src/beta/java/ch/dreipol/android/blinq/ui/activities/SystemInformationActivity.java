package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;

import ch.dreipol.android.blinq.R;


public class SystemInformationActivity extends BaseBlinqActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_information);
    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }

}
