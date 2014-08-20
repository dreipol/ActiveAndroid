package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 18/08/14.
 */
public class DarkSeparatorView extends View {
    public DarkSeparatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.dark_separator));
    }
}
