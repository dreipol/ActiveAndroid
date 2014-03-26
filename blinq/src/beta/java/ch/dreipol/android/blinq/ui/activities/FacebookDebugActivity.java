package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 26.03.14.
 */
public class FacebookDebugActivity extends BaseBlinqActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_debug);
        findViewById(R.id.facebook_name);

    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }
}
