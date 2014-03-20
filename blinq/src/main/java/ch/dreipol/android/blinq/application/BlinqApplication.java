package ch.dreipol.android.blinq.application;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import ch.dreipol.android.blinq.util.Bog;

/**
 * Created by phil on 19.03.14.
 */
public class BlinqApplication extends Application implements Application.ActivityLifecycleCallbacks {

    public static final String HOCKEY_APP_IDENTIFIER = "b6515d8c05bcc1ba45256f9dae09cfc9";


    @Override
    public void onCreate() {
        super.onCreate();
        Bog.v(Bog.Category.SYSTEM, "Starting BLINQ: " + getApplicationContext().getPackageName());
        registerActivityLifecycleCallbacks(this);
        Bog.v(Bog.Category.SYSTEM, "BLINQ Flavour is: " + getFlavour());
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Bog.v(Bog.Category.UI, "Created Activity: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Bog.v(Bog.Category.UI, "Started Activity: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Bog.v(Bog.Category.UI, "Resumed Activity: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Bog.v(Bog.Category.UI, "Paused Activity: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Bog.v(Bog.Category.UI, "Stopped Activity: " + activity.getLocalClassName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Bog.v(Bog.Category.UI, "Saved Activity Instance State: " + activity.getLocalClassName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Bog.v(Bog.Category.UI, "Destroyed Activity: " + activity.getLocalClassName());
    }


    public BlinqApplicationFlavour getFlavour() {
        BlinqApplicationFlavour result = BlinqApplicationFlavour.PRODUCTION;
        try {
            ApplicationInfo ai = getApplicationContext().getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String stringFlavour = bundle.getString("BLINQ_FLAVOUR");
            result = BlinqApplicationFlavour.valueOf(stringFlavour);
        } catch (NullPointerException e) {
            Bog.e(Bog.Category.SYSTEM, "Could not get Metadata", e);
        } catch (PackageManager.NameNotFoundException e) {
            Bog.e(Bog.Category.SYSTEM, "Could not get Metadata", e);
        }
        return result;
    }
}
