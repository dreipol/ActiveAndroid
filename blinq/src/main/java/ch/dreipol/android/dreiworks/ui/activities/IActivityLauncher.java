package ch.dreipol.android.dreiworks.ui.activities;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by phil on 20.03.14.
 */
public interface IActivityLauncher {
    void startActivity(Class<? extends Activity> activityClass, ActivityTransitionType direction, Bundle parameters);

    void startActivity(Class<? extends Activity> activityClass, ActivityTransitionType direction);

    void startActivityForResult(Class<? extends Activity> activityClass, ActivityTransitionType direction, Bundle parameters, int requestCode);

    void startActivityForResult(Class<? extends Activity> activityClass, ActivityTransitionType direction, int requestCode);
}
