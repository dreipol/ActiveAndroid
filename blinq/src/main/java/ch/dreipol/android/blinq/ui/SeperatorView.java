package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 18/08/14.
 */
public class SeperatorView extends View{
    public SeperatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.seperator));
    }
}
