package ch.dreipol.android.blinq.services;

import android.widget.ImageView;

import ch.dreipol.android.blinq.services.model.Photo;

/**
 * Created by melbic on 18/07/14.
 */
public interface IImageCacheService extends IService {

    public rx.subjects.BehaviorSubject<ch.dreipol.android.blinq.services.model.LoadingInfo> displayImage(String imageURI, ImageView view);
    public rx.Observable<ch.dreipol.android.blinq.services.model.LoadingInfo> displayPhoto(Photo photo, ImageView view);
}
