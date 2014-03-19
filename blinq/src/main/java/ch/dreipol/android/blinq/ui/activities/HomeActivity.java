package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;

import ch.dreipol.android.blinq.R;

public class HomeActivity extends BlinqBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
