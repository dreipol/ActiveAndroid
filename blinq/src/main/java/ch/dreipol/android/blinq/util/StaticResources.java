package ch.dreipol.android.blinq.util;

import android.content.Context;

/**
 * Created by phil on 21.03.14.
 */
public class StaticResources {
    public static int convertDisplayPointsToPixel(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
