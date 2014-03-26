package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;
import android.view.View;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;

/**
 * Created by phil on 26.03.14.
 */
public class NetworkDebugActivity extends BaseBlinqActivity {

    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_debug);

        findButtonWithId(R.id.test_raw_swarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppService.getInstance().getNetworkService().getSwarm();
            }
        });

        findButtonWithId(R.id.test_raw_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppService.getInstance().getNetworkService().getMe();
            }
        });
    }
}
