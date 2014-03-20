package ch.dreipol.android.dreiworks.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by phil on 19.03.14.
 */
public abstract class BaseActivity extends Activity {

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }


    protected abstract boolean shouldStartDebugActivity();


    private void startDebugActivity() {
        if (shouldStartDebugActivity()) {
            Intent intent = null;
            try {
                intent = new Intent(getApplicationContext(), Class.forName("ch.dreipol.android.blinq.ui.activities.DebugActivity"));
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}