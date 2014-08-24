package ch.dreipol.android.blinq.ui.lists;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.model.facebook.FacebookPhoto;
import ch.dreipol.android.blinq.util.Bog;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by phil on 22/08/14.
 */
public class FacebookAlbumListItemView extends RelativeLayout {
    private ImageView mImageView;
    private TextView mTextView;
    private Subscription mImageSubscription;

    public FacebookAlbumListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = AppService.getInstance().getRuntimeService().getLayoutInflator(context);
        inflater.inflate(R.layout.ui_album_list_item, this, true);

        mImageView = (ImageView) findViewById(R.id.photo);
    }

    public FacebookAlbumListItemView(Context context) {
        this(context, null);
    }

    public void setText(String name) {
        getTextView().setText(name);
    }

    public TextView getTextView() {
        if (mTextView == null) {
            mTextView = (TextView) findViewById(R.id.text);
        }
        return mTextView;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public void setImage(String coverId) {
        setLoading(true);
        if (mImageSubscription != null) {
            mImageSubscription.unsubscribe();

        }
        if (coverId != null) {
            mImageSubscription = AppService.getInstance().getFacebookService().getPhotoForId(coverId).subscribe(new Action1<FacebookPhoto>() {
                @Override
                public void call(FacebookPhoto facebookPhotoSource) {
                    AppService.getInstance().getImageCacheService().displayImage(facebookPhotoSource.getPicture(), getImageView());
                    Bog.d(Bog.Category.FACEBOOK, "Facebook: " + facebookPhotoSource.toString());
                    setLoading(false);

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    setLoading(false);
                }
            });
        }


    }

    private void setLoading(boolean isLoading) {

    }
}
