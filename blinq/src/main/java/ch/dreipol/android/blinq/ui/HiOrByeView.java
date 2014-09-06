package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.util.Bog;

/**
 * Created by phil on 05/09/14.
 */
public class HiOrByeView extends ViewGroup {
    private final RelativeLayout mFirstViewContainer;
    private final RelativeLayout mSecondViewContainer;
    private float mInitialX;
    private float mXTranslation;

    public HiOrByeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mSecondViewContainer = new RelativeLayout(context, null);
        addView(mSecondViewContainer);

        mFirstViewContainer = new RelativeLayout(context, null);
        addView(mFirstViewContainer);

        mSecondViewContainer.setBackgroundColor(getResources().getColor(R.color.debug_blue));
        mFirstViewContainer.setBackgroundColor(getResources().getColor(R.color.debug_orange));
//        mSecondViewContainer.setBackgroundColor(R.color.);


        disableTouchTheft(this);
        disableTouchTheft(mSecondViewContainer);
        disableTouchTheft(mFirstViewContainer);



        mInitialX = 0;
        mXTranslation = 0;

//        this.setOnTouchListener(new OnTouchListener() {
//
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//
//                float eventX = event.getX();
//
//
//                int actionMasked = event.getActionMasked();
//
//                if (actionMasked == MotionEvent.ACTION_DOWN) {
//                    return false;
//                }
//
//
//                float translation = eventX - mInitialX;
//
//                switch (actionMasked) {
//                    case MotionEvent.ACTION_DOWN:
//                        mInitialX = eventX - mXTranslation;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
////                        centerViewUpdated(translation);
//                        break;
//                    case MotionEvent.ACTION_UP:
////                        centerViewUpdateFinished();
//                        mXTranslation = translation;
//                        break;
//                }
//                Bog.d(Bog.Category.UI, "translation: " + eventX);
//
//                return false;
//            }
//        });
    }

    public static void disableTouchTheft(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                Bog.d(Bog.Category.UI, "DISALLOW THAT SHIT");
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mFirstViewContainer.layout(l,t,r,b);
        mSecondViewContainer.layout(l,t,r,b);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getParent().requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Bog.d(Bog.Category.UI, "HI OR BY INTERCEPT");
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Bog.d(Bog.Category.UI, "HI OR BY ONTOUCH");
        return true;

    }
}
