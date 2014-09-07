package ch.dreipol.android.blinq.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.dreiworks.ui.activities.ActivityTransitionType;
import ch.dreipol.android.dreiworks.ui.activities.BaseActivity;

/**
 * Created by phil on 20.03.14.
 */
public abstract class BaseBlinqActivity extends BaseActivity {


    private UiLifecycleHelper mUiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session.StatusCallback callback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        };

        mUiHelper = new UiLifecycleHelper(this, callback);
        mUiHelper.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUiHelper.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
       mUiHelper.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUiHelper.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mUiHelper.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUiHelper.onActivityResult(requestCode, resultCode, data);
    }


    protected void onSessionStateChange(Session session, SessionState state, Exception exception) {
        Bog.v(Bog.Category.FACEBOOK, state.toString());
        AppService.getInstance().getFacebookService().updateSessionState(state);
    }


    @Override
    public void overrideTransitionForAnimationDirection(ActivityTransitionType transitionType) {
        switch (transitionType) {
            case CROSSFADE:
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case TO_LEFT:
                overridePendingTransition(R.anim.right_out_to_left, R.anim.left_to_left_out);
                break;
            case TO_RIGHT:
                overridePendingTransition(R.anim.left_out_to_left, R.anim.left_to_right_out);
                break;
            case DEFAULT:
                break;

        }
    }
}
