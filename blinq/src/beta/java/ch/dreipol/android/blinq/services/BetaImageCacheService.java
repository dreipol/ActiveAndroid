package ch.dreipol.android.blinq.services;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import ch.dreipol.android.blinq.application.ImageCacheService;

/**
 * Created by melbic on 03/09/14.
 */
public class BetaImageCacheService extends ImageCacheService {
    @Override
    protected ImageLoaderConfiguration.Builder getImageLoaderConfigurationBuilder(Context context, DisplayImageOptions defaultDisplayOptions) {
        return super.getImageLoaderConfigurationBuilder(context, defaultDisplayOptions).writeDebugLogs();
    }
}
