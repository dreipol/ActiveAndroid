package ch.dreipol.android.blinq.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import ch.dreipol.android.blinq.debug.ui.activities.DebugActivity;

/**
 * Created by phil on 19.03.14.
 */
public abstract class BlinqBaseActivity extends Activity {

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TODO: Phil, please make debug only and with more fingers/taps
        mGestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                startDebugActivity();
                return super.onDoubleTap(e);
            }
        });
    }

    protected void startDebugActivity() {
        Intent intent = new Intent(getApplicationContext(), DebugActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }
}
