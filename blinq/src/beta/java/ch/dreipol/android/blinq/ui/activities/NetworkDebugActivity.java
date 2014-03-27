package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.Profile;
import ch.dreipol.android.blinq.util.Bog;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
                HashMap aMap = new HashMap();
                aMap.put("radius", 200);
                aMap.put("min_age", 18);
                aMap.put("max_age", 69  );
                AppService.getInstance().getNetworkService().getSwarm(aMap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(printProfileAction());
            }
        });

        findButtonWithId(R.id.test_raw_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppService.getInstance().getNetworkService().getMe()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(printProfileAction());
            }
        });
    }

    private static Action1<Profile> printProfileAction() {
        return new Action1<Profile>() {
            @Override
            public void call(Profile profile) {
                Bog.v(Bog.Category.NETWORKING, profile.toString());
            }
        };
    }
}
