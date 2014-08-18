package ch.dreipol.android.dreiworks.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by phil on 19.03.14.
 */
public abstract class BaseActivity extends FragmentActivity implements IActivityLauncher {

    private static final String DATA_SOURCE_CLASS = "SOURCE_CLASS";
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


    protected View.OnClickListener createActivityClickListener(final Class<? extends Activity> activityClass, final ActivityTransitionType direction) {
        return createActivityClickListener(activityClass, direction, null);
    }

    protected View.OnClickListener createActivityClickListener(final Class<? extends Activity> activityClass, final ActivityTransitionType direction, final Bundle parameters) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(activityClass, direction, parameters);
            }
        };
    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, ActivityTransitionType direction, Bundle parameters) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        if (parameters == null) {
            parameters = new Bundle();
        }
        parameters.putString(DATA_SOURCE_CLASS, this.getLocalClassName());
        intent.putExtras(parameters);

        startActivity(intent);
        overrideTransitionForAnimationDirection(direction);
    }


    @Override
    public void startActivity(Class<? extends Activity> activityClass, ActivityTransitionType direction) {
        startActivity(activityClass, direction, null);
    }


    public abstract void overrideTransitionForAnimationDirection(ActivityTransitionType transitionType);

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

    protected Button findButtonWithId(int identifier) {
        return (Button) findViewById(identifier);
    }
}