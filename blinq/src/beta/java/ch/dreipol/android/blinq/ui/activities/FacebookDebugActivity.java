package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IFacebookService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.dreiworks.ui.activities.ActivityTransitionType;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by phil on 26.03.14.
 */
public class FacebookDebugActivity extends BaseBlinqActivity {


    private Subscription mSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_debug);
        final TextView name = (TextView) findViewById(R.id.facebook_name);
        final TextView ident = (TextView) findViewById(R.id.facebook_id);
        final TextView token = (TextView) findViewById(R.id.facebook_token);
        final TextView valid = (TextView) findViewById(R.id.facebook_token_valid_until);
        final TextView state = (TextView) findViewById(R.id.facebook_status);

        final Button requestButton = (Button) findViewById(R.id.get_albums);


        mSubscribe = AppService.getInstance().getFacebookService().subscribeToSessionState().subscribe(new Action1<FacebookService.FacebookServiceInfo>() {
            @Override
            public void call(FacebookService.FacebookServiceInfo serviceInfo) {

                if (serviceInfo.status.getValue() > FacebookService.FacebookServiceStatus.LOGGED_OUT.getValue()) {

                    Session activeSession = Session.getActiveSession();
                    IFacebookService facebookService = AppService.getInstance().getFacebookService();


                    valid.setText(activeSession.getExpirationDate().toString());
                    token.setText(facebookService.getAccessToken());
                    requestButton.setVisibility(View.VISIBLE);
                } else {
                    valid.setText("");
                    token.setText("");
                    requestButton.setVisibility(View.GONE);
                }

                name.setText(serviceInfo.user.name);
                ident.setText(serviceInfo.user.id);
                state.setText(serviceInfo.status.toString());
                Toast.makeText(getApplicationContext(),
                        "Facebook: " + serviceInfo.status.toString(),
                        Toast.LENGTH_SHORT)
                        .show();


            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FacebookAlbumsActivity.class, ActivityTransitionType.DEFAULT);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscribe.unsubscribe();

    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }
}
