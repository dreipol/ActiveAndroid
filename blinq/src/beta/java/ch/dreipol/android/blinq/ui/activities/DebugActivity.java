package ch.dreipol.android.blinq.ui.activities;

import android.os.Bundle;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.ui.LoaderView;
import ch.dreipol.android.dreiworks.ui.activities.ActivityTransitionType;

public class DebugActivity extends BaseBlinqActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        findButtonWithId(R.id.debug_system_information).setOnClickListener(createActivityClickListener(SystemInformationActivity.class, ActivityTransitionType.DEFAULT));
        findButtonWithId(R.id.debug_network_services).setOnClickListener(createActivityClickListener(NetworkDebugActivity.class, ActivityTransitionType.DEFAULT));
        findButtonWithId(R.id.debug_facebook_button).setOnClickListener(createActivityClickListener(FacebookDebugActivity.class, ActivityTransitionType.DEFAULT));


        LoaderView loaderDarkBig = (LoaderView) findViewById(R.id.loader_view_dark_big);
        LoaderView loaderDarkSmall = (LoaderView) findViewById(R.id.loader_view_dark_small);
        LoaderView loaderLightBig = (LoaderView) findViewById(R.id.loader_view_light_big);
        LoaderView loaderLightSmall = (LoaderView) findViewById(R.id.loader_view_light_small);

        loaderDarkBig.setLoaderType(LoaderView.LoaderType.DARK, LoaderView.LoaderSize.BIG);
        loaderDarkSmall.setLoaderType(LoaderView.LoaderType.DARK, LoaderView.LoaderSize.SMALL);
        loaderLightBig.setLoaderType(LoaderView.LoaderType.LIGHT, LoaderView.LoaderSize.BIG);
        loaderLightSmall.setLoaderType(LoaderView.LoaderType.LIGHT, LoaderView.LoaderSize.SMALL);

    }

    @Override
    protected boolean shouldStartDebugActivity() {
        return false;
    }

}
