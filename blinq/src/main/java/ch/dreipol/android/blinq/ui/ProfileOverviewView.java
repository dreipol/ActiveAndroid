package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 19/08/14.
 */
public class ProfileOverviewView extends ViewGroup {

    public ProfileOverviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            Resources r = getResources();
            int topColor = r.getColor(R.color.blinq_blue_4a799e);
            int bottomColor = r.getColor(R.color.blinq_red_f9395C);
            setGradient(topColor, bottomColor);
        }

    }

    public void setGradient(int topColor, int bottomColor) {
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{topColor, bottomColor});
        g.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        setBackgroundDrawable(g);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
