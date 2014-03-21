package ch.dreipol.android.blinq.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by phil on 21.03.14.
 */
public class StaticResources {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int convertDisplayPointsToPixel(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @SuppressLint("NewApi")
    public static int generateViewId() {

        if (Build.VERSION.SDK_INT < 17) {
            for (;;) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }

    }
}
