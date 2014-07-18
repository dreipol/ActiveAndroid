package ch.dreipol.android.blinq.services;

import android.widget.ImageView;

import ch.dreipol.android.blinq.services.model.Photo;

/**
 * Created by melbic on 18/07/14.
 */
public interface IImageCacheService extends IService {

    public void displayImage(String imageURI, ImageView view);
    public void displayPhoto(Photo photo, ImageView view);
}
