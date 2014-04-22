package ch.dreipol.android.blinq.ui.loaders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 22.04.14.
 */
public class SearchLoader extends RelativeLayout{

    public SearchLoader(Context context, AttributeSet attrs) {
        super(context, attrs);

        ImageView rotationMask= new ImageView(context);
        rotationMask.setImageDrawable(getResources().getDrawable(R.drawable.searchding_drehend));
        rotationMask.setAdjustViewBounds(true);
        addView(rotationMask, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        RotateAnimation rotator = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotator.setRepeatCount(10000);

        rotator.setDuration(5000);
        rotator.setRepeatMode(Animation.INFINITE);
        rotationMask.startAnimation(rotator);



        ImageView mask= new ImageView(context);
        mask.setImageDrawable(getResources().getDrawable(R.drawable.searchding_darueber));
        mask.setAdjustViewBounds(true);
        addView(mask, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


//        int[] colors =new int []{getResources().getColor(R.color.blinq_light_grey), getResources().getColor(R.color.blinq_dark_grey)};
        Random rnd = new Random();
        int color1 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int color2 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        int[] colors =new int []{color1,color2};
        GradientDrawable backgroundGradient  = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        setBackgroundDrawable(backgroundGradient);

    }



}
