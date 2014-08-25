package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;

/**
 * Created by phil on 25/08/14.
 */
public class LoaderView extends RelativeLayout {


    private final ImageView mLoader;
    private final ImageView mOverlay;

    public LoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);


        LayoutInflater layoutInflator = AppService.getInstance().getRuntimeService().getLayoutInflator(context);
        layoutInflator.inflate(R.layout.ui_loader, this);


        mLoader = (ImageView) findViewById(R.id.loader_image);
        mOverlay = (ImageView) findViewById(R.id.overlay);

        RotateAnimation rotator = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotator.setInterpolator(new LinearInterpolator());
        rotator.setRepeatCount(10000);
        rotator.setDuration(2000);
        rotator.setRepeatMode(Animation.INFINITE);

        mLoader.startAnimation(rotator);
    }

    public void setLoaderType(LoaderType type, LoaderSize size) {

        int overlayResource = -10;
        int loaderResource = -10;

        switch (type) {
            case DARK:
                switch (size) {
                    case BIG:
                        overlayResource = R.drawable.icon_loader_dark_big_overlay;
                        loaderResource = R.drawable.icon_loader_dark_big_animated;
                        break;
                    case SMALL:
                        overlayResource = R.drawable.icon_loader_dark_small_overlay;
                        loaderResource = R.drawable.icon_loader_dark_small_animated;
                        break;
                }
                break;
            case LIGHT:
                switch (size) {
                    case BIG:
                        overlayResource = R.drawable.icon_loader_light_big_overlay;
                        loaderResource = R.drawable.icon_loader_light_big_animated;
                        break;
                    case SMALL:
                        overlayResource = R.drawable.icon_loader_light_small_overlay;
                        loaderResource = R.drawable.icon_loader_light_small_animated;
                        break;
                }
                break;
        }

        mLoader.setImageDrawable(getResources().getDrawable(loaderResource));
        mOverlay.setImageDrawable(getResources().getDrawable(overlayResource));
    }

    public enum LoaderType {
        DARK,
        LIGHT
    }

    public enum LoaderSize {
        BIG,
        SMALL
    }

}
