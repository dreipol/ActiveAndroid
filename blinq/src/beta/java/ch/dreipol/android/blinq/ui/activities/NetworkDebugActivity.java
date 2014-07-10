package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IValueStoreService;
import ch.dreipol.android.blinq.services.Profile;
import ch.dreipol.android.blinq.services.SwarmManager;
import ch.dreipol.android.blinq.util.Bog;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by phil on 26.03.14.
 */
public class NetworkDebugActivity extends BaseBlinqActivity {
    SwarmManager mManager;

    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_debug);
        IValueStoreService valueStore = AppService.getInstance().getValueStore();
        valueStore.put("radius", 200);
        valueStore.put("min_age", 18);
        valueStore.put("max_age", 69);
        mManager = new SwarmManager();
        findButtonWithId(R.id.test_raw_swarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap aMap = new HashMap();
                aMap.put("radius", 200);
                aMap.put("min_age", 18);
                aMap.put("max_age", 69);
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
                        .subscribe(printProfileAction(), new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {

                                    }
                                },
                                new Action0() {
                                    @Override
                                    public void call() {

                                        Bog.v(Bog.Category.NETWORKING, "Finished");
                                    }
                                }
                        );
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
