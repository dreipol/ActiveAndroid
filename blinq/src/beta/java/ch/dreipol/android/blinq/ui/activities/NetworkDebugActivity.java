package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IProfileListener;
import ch.dreipol.android.blinq.services.ISwarmIterator;
import ch.dreipol.android.blinq.services.IValueStoreService;
import ch.dreipol.android.blinq.services.model.Photo;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.services.SwarmManager;
import ch.dreipol.android.blinq.util.Bog;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
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
        valueStore.put("radius", -1);
        valueStore.put("min_age", 18);
        valueStore.put("max_age", 69);
        mManager = new SwarmManager();
        final ISwarmIterator swarmIterator = mManager.firstIterator();
        swarmIterator.setProfileListener(new IProfileListener() {
            @Override
            public void setProfile(Profile profile) {
                final Photo photo = profile.getPhotos().get(0);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppService.getInstance().getImageCacheService().displayPhoto(photo, (ImageView) findViewById(R.id.test_imageView));
                    }
                });
            }
        });

        findButtonWithId(R.id.test_swarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.reloadSwarm();
            }
        });

        findButtonWithId(R.id.test_say_hi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swarmIterator.hi();
            }
        });
        findButtonWithId(R.id.test_say_bye).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swarmIterator.bye();
            }
        });

        findButtonWithId(R.id.test_matches).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppService.getInstance().getNetworkService().loadMatches();
            }
        });
        findButtonWithId(R.id.test_raw_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppService.getInstance().getAccountService().signup()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(printProfileAction(), new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Bog.e(Bog.Category.NETWORKING, "Error: " + throwable.toString());

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
        findButtonWithId(R.id.update_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppService.getInstance().getAccountService().update();
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
