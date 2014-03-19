package ch.dreipol.android.blinq.ui.viewgroups;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 19.03.14.
 */
public class BlinqDrawerLayout extends ViewGroup {

    private View leftView;
    private View rightView;
    private View centerView;

    public BlinqDrawerLayout(Context context) {
        super(context);
        centerView = new RelativeLayout(context);
        centerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        centerView.setBackgroundColor(getResources().getColor(R.color.debug_orange));

        addView(centerView);

    }

    public BlinqDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
