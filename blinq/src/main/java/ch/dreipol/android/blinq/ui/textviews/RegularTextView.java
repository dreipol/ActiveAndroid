package ch.dreipol.android.blinq.ui.textviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import ch.dreipol.android.blinq.util.StaticResources;

/**
 * Created by phil on 21.03.14.
 */
public class RegularTextView extends TextView {

    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface font = StaticResources.regular(this, context.getAssets());
        setTypeface(font, Typeface.NORMAL);
    }
}