package ch.dreipol.android.dreiworks.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.SettingsProfile;
import ch.dreipol.android.blinq.ui.activities.FacebookPhotoPickerActivity;
import ch.dreipol.android.blinq.ui.fragments.LoadingState;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by phil on 19.03.14.
 */
public abstract class BaseActivity extends FragmentActivity implements IActivityLauncher {

    private static final String DATA_SOURCE_CLASS = "SOURCE_CLASS";
    public static final int FACEBOOK_PHOTOPICKER_REQUEST = 0;
    public static final String PHOTOSOURCE_POSITION = "ch.dreipol.android.blinq.profile.photosource_position";
    private GestureDetector mGestureDetector;
    protected PublishSubject<LoadingInfo> mGuiLoadStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGuiLoadStatus = PublishSubject.create();

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
        Intent intent = getIntent(activityClass, parameters);

        startActivity(intent);
        overrideTransitionForAnimationDirection(direction);
    }


    @Override
    public void startActivity(Class<? extends Activity> activityClass, ActivityTransitionType direction) {
        startActivity(activityClass, direction, null);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, ActivityTransitionType direction, Bundle parameters, int requestCode) {
        Intent intent = getIntent(activityClass, parameters);

        startActivityForResult(intent, requestCode);
        overrideTransitionForAnimationDirection(direction);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, ActivityTransitionType direction, int requestCode) {
        startActivityForResult(activityClass, direction, null, requestCode);
    }

    protected Intent getIntent(Class<? extends Activity> activityClass, Bundle parameters) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        if (parameters == null) {
            parameters = new Bundle();
        }
        parameters.putString(DATA_SOURCE_CLASS, this.getLocalClassName());
        intent.putExtras(parameters);
        return intent;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FACEBOOK_PHOTOPICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                String photoID = data.getStringExtra(FacebookPhotoPickerActivity.FACEBOOK_PHOTO_ID);
                int position = getIntent().getIntExtra(PHOTOSOURCE_POSITION, 4);
                mGuiLoadStatus.onNext(new LoadingInfo(LoadingState.LOADING));
                AppService.getInstance().getNetworkService().createOrUpdatePhoto(photoID, position).subscribe(new Action1<SettingsProfile>() {
                    @Override
                    public void call(SettingsProfile settingsProfile) {
                        LoadingInfo v = new LoadingInfo(LoadingState.LOADED);
                        mGuiLoadStatus.onNext(v);
                    }
                });


            }
            getIntent().removeExtra(PHOTOSOURCE_POSITION);

        }
    }

}