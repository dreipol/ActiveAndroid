package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
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

    private final Button mEditButton;
    private final Button mDeleteButton;
    private final Button mAddButton;

    public ProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = AppService.getInstance().getRuntimeService().getLayoutInflator(context);
        inflater.inflate(R.layout.ui_profil_image, this, true);

        mImageView = (ImageView) findViewById(R.id.image);
        mBackground = (RelativeLayout) findViewById(R.id.background);
//        mBackground.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mLoaderView = (LoaderView) findViewById(R.id.loader);
        mLoaderView.setLoaderType(LoaderView.LoaderType.DARK, LoaderView.LoaderSize.BIG);

        mEditButton = (Button) findViewById(R.id.edit_button);
        mDeleteButton = (Button) findViewById(R.id.delete_button);
        mAddButton = (Button) findViewById(R.id.add_button);

    }

    public ProfileImageView(Context context) {
        this(context, null);
    }


    public ImageView getImageView() {
        return mImageView;
    }

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
        switch (type) {
            case BIG:
                mBackground.setPadding(s15dp, s10dp, s10dp, s15dp);
                mAddButton.setVisibility(GONE);
                mDeleteButton.setVisibility(GONE);
                mEditButton.setVisibility(VISIBLE);
                break;

            case SMALL:
                backgroundId = R.drawable.detailscreen_picture_small_standard;
                mBackground.setPadding(s8dp, s5dp, s5dp, s8dp);
                size = LoaderView.LoaderSize.SMALL;
                s = StaticResources.convertDisplayPointsToPixel(getContext(), 77);
                mAddButton.setVisibility(GONE);
                mDeleteButton.setVisibility(VISIBLE);
                mEditButton.setVisibility(GONE);

                break;
        }

        layoutParams.width = s;
        layoutParams.height = s;

        mImageView.setLayoutParams(layoutParams);

        mBackground.setBackgroundDrawable(getResources().getDrawable(backgroundId));


        mLoaderView.setLoaderType(color, size);

    }
}
