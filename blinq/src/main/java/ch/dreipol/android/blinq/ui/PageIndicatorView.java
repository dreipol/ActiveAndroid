package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 18/08/14.
 */
public class PageIndicatorView extends LinearLayout {

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        if(isInEditMode()){
            setNumberOfPages(5);
            setActivePage(3);
        }
    }


    public void setNumberOfPages(Integer numberOfPages) {
        removeAllViews();
        for (int i = 0; i < numberOfPages; i++) {
            View view = new View(getContext());
            view.setSelected(false);
            int size = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);

            int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

            params.setMargins(margin,0,margin,0);
            view.setLayoutParams(params);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.blinq_page_indicator));
            addView(view);
        }

    }

    public void setActivePage(Integer activePage) {

        for (int i = 0; i < getChildCount() ; i++) {

            getChildAt(i).setSelected(false);
        }

        getChildAt(activePage).setSelected(true);
    }


}
