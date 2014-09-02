package ch.dreipol.android.blinq.application;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IImageCacheService;
import ch.dreipol.android.blinq.services.impl.BaseService;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.Photo;
import ch.dreipol.android.blinq.ui.fragments.LoadingState;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by melbic on 18/07/14.
 */
public class ImageCacheService extends BaseService implements IImageCacheService {

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

    public BehaviorSubject<LoadingInfo> displayImage(String imageURI, ImageView view) {
        final BehaviorSubject<LoadingInfo> subject = BehaviorSubject.create();

        mImageLoader.displayImage(imageURI, view, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                subject.onNext(new LoadingInfo(LoadingState.LOADING));
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                subject.onNext(new LoadingInfo(LoadingState.ERROR));
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                subject.onNext(new LoadingInfo(LoadingState.LOADED));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                subject.onNext(new LoadingInfo(LoadingState.CANCELED));
            }
        });
        return subject;
    }

    @Override
    public Observable<LoadingInfo> displayPhoto(Photo photo, final ImageView view) {
        Observable<Photo> startingObservable = Observable.just(photo);
        if (photo.isExpired()) {
            startingObservable = renew(photo).subscribeOn(Schedulers.io());
        }
        return startingObservable.observeOn(AndroidSchedulers.mainThread()).concatMap(new Func1<Photo, Observable<LoadingInfo>>() {
            @Override
            public Observable<LoadingInfo> call(Photo photo) {
                String URI = photo.getThumbId();
                return ImageCacheService.this.displayImage(URI, view);
            }
        });

    }

    public Observable<Photo> renew(Photo photo) {
        return getService().getNetworkService().renewPhotoSource(photo).doOnNext(photo.getUpdateAction());
    }
}
