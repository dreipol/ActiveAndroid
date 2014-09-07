package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 20/08/14.
 */
public class LightSeparatorView extends View {

    public LightSeparatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_separator));
    }
}
