package ch.dreipol.android.blinq.ui.lists;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.model.facebook.FacebookPhoto;
import ch.dreipol.android.blinq.util.Bog;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by phil on 24/08/14.
 */
public class FacebookPhotoListItemView extends RelativeLayout {
    private ImageView mImageView;

    public FacebookPhotoListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = AppService.getInstance().getRuntimeService().getLayoutInflator(context);
        inflater.inflate(R.layout.ui_photo_list_item, this, true);

        mImageView = (ImageView) findViewById(R.id.image);
    }

    public FacebookPhotoListItemView(Context context) {
        this(context, null);
    }



    public ImageView getImageView() {
        return mImageView;
    }

    public void setImage(String photoId) {
        setLoading(true);
        ImageView imageView = getImageView();
        imageView.setImageDrawable(null);
        AppService.getInstance().getImageCacheService().displayImage(photoId, imageView);
        setLoading(false);

    }

    private void setLoading(boolean isLoading) {

    }


}
