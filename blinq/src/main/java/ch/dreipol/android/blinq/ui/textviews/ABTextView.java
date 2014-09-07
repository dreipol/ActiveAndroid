package ch.dreipol.android.blinq.ui.textviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import ch.dreipol.android.blinq.util.StaticResources;

/**
 * Created by phil on 20/08/14.
 */
public class ABTextView extends TextView {
    public ABTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface font = StaticResources.archerBold(this, context.getAssets());
        setTypeface(font, Typeface.NORMAL);
    }
}
