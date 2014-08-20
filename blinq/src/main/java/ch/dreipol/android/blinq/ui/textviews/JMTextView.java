package ch.dreipol.android.blinq.ui.textviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import ch.dreipol.android.blinq.util.StaticResources;

/**
 * Created by phil on 20/08/14.
 */
public class JMTextView extends TextView {
    public JMTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface font = StaticResources.justMeAgain(this, context.getAssets());
        setTypeface(font, Typeface.NORMAL);
    }
}
