package ch.dreipol.android.blinq.application;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IImageCacheService;
import ch.dreipol.android.blinq.services.impl.BaseService;
import ch.dreipol.android.blinq.services.model.Photo;

/**
 * Created by melbic on 18/07/14.
 */
public class ImageCache extends BaseService implements IImageCacheService {

    private ImageLoader mImageLoader;

    @Override
    public void dispose() {

    }

    @Override
    public void setup(AppService appService) {
        super.setup(appService);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(appService.getContext()).build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(config);
    }

    public void displayImage(String imageURI, ImageView view) {
        mImageLoader.displayImage(imageURI, view);
    }

    @Override
    public void displayPhoto(Photo photo, ImageView view) {
        String URI = photo.getProfile_id();
        displayImage(URI, view);
    }
}
