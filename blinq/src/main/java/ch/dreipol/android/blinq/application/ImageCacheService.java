package ch.dreipol.android.blinq.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.dreipol.android.blinq.R;
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
        Context context = appService.getContext();
        DisplayImageOptions defaultDisplayOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_loader_dark_big_animated) // resource or drawable
                .showImageForEmptyUri(R.drawable.icon) // resource or drawable
                .showImageOnFail(R.drawable.icon_fail) // resource or drawable
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = getImageLoaderConfigurationBuilder(context, defaultDisplayOptions)
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(config);
    }

    protected ImageLoaderConfiguration.Builder getImageLoaderConfigurationBuilder(Context context, DisplayImageOptions defaultDisplayOptions) {
        File cacheDir = StorageUtils.getIndividualCacheDirectory(context);
        BlinqImageFileNameGenerator fileNameGenerator = new BlinqImageFileNameGenerator();
        int diskCacheMaxSize = 0;
        return new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCache(new LruDiscCache(cacheDir, fileNameGenerator, diskCacheMaxSize))
                .diskCacheFileNameGenerator(fileNameGenerator)
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .memoryCache(new LruMemoryCache(4 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .defaultDisplayImageOptions(defaultDisplayOptions)
                .tasksProcessingOrder(QueueProcessingType.LIFO);
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

    class BlinqImageFileNameGenerator extends HashCodeFileNameGenerator {

        private final Pattern mPattern;

        BlinqImageFileNameGenerator() {
            super();
            mPattern = Pattern.compile("\\/\\/.*?\\/(.*?)(?=\\?)");
        }

        @Override
        public String generate(String imageUri) {
            String uri = imageUri;
            Matcher matcher = mPattern.matcher(uri);
            if (matcher.find()) {
                uri = matcher.group(1);
            }
            String hash = super.generate(uri);
            return hash;
        }
    }
}
