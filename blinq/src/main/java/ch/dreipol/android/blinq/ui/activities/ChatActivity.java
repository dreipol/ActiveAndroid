package ch.dreipol.android.blinq.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ch.dreipol.android.blinq.R;

public class ChatActivity extends BaseBlinqActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }


}
