package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.util.StaticResources;

/**
 * Created by phil on 02/09/14.
 */
public class ProfileImageView extends RelativeLayout {

    private final ImageView mImageView;
    private final RelativeLayout mBackground;
    private final LoaderView mLoaderView;

    public ProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = AppService.getInstance().getRuntimeService().getLayoutInflator(context);
        inflater.inflate(R.layout.ui_profil_image, this, true);

        mImageView = (ImageView) findViewById(R.id.image);
        mBackground = (RelativeLayout) findViewById(R.id.background);
        mBackground.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mLoaderView = (LoaderView) findViewById(R.id.loader);
        mLoaderView.setLoaderType(LoaderView.LoaderType.DARK, LoaderView.LoaderSize.BIG);

    }

    public ProfileImageView(Context context) {
        this(context, null);
    }


    public ImageView getImageView() {
        return mImageView;
    }

//    android:paddingBottom="15dp"
//    android:paddingLeft="15dp"
//    android:paddingRight="10dp"
//    android:paddingTop="10dp"
//
    public void setType(ProfileImageViewType type) {
        int s = StaticResources.convertDisplayPointsToPixel(getContext(), 160);
        int s10dp = StaticResources.convertDisplayPointsToPixel(getContext(), 12);
        int s15dp = StaticResources.convertDisplayPointsToPixel(getContext(), 18);

        int s5dp = StaticResources.convertDisplayPointsToPixel(getContext(), 8);
        int s8dp = StaticResources.convertDisplayPointsToPixel(getContext(), 10);

        ViewGroup.LayoutParams layoutParams = mImageView.getLayoutParams();

        LoaderView.LoaderType color = LoaderView.LoaderType.DARK;
        LoaderView.LoaderSize size = LoaderView.LoaderSize.BIG;

        int backgroundId = R.drawable.detailscreen_picture_large_standard;
        switch (type){
            case BIG:
                mBackground.setPadding(s15dp,s10dp,s10dp,s15dp);
                break;

            case SMALL:
                backgroundId  = R.drawable.detailscreen_picture_small_standard;
                mBackground.setPadding(s8dp,s5dp,s5dp,s8dp);
                size = LoaderView.LoaderSize.SMALL;
                s = StaticResources.convertDisplayPointsToPixel(getContext(), 77);
                break;
        }

        layoutParams.width = s;
        layoutParams.height = s;

        mImageView.setLayoutParams(layoutParams);

        mBackground.setBackgroundDrawable(getResources().getDrawable(backgroundId));


        mLoaderView.setLoaderType(color, size);

    }
}
