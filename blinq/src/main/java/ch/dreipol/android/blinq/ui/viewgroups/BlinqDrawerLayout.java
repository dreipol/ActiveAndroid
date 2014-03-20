package ch.dreipol.android.blinq.ui.viewgroups;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.util.Bog;

/**
 * Created by phil on 19.03.14.
 */
public class BlinqDrawerLayout extends ViewGroup {

    private Interpolator mInterpolator;
    private View mBackgroundView;
    private View mLeftView;
    private View mRightView;
    private View mCenterView;
    private int mLeft;
    private int mRight;
    private DrawerSnap mSnap;
    private float mBaseAlpha;

    public BlinqDrawerLayout(Context context) {
        super(context);

    }

    public BlinqDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundView = new View(context);
        mBackgroundView.setBackgroundColor(getResources().getColor(R.color.blinq_black));
        addView(mBackgroundView);

        mLeftView = new RelativeLayout(context);
        mLeftView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mLeftView.setBackgroundColor(getResources().getColor(R.color.debug_blue));
        addView(mLeftView);

        mRightView = new RelativeLayout(context);
        mRightView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRightView.setBackgroundColor(getResources().getColor(R.color.debug_green));
        addView(mRightView);

        mCenterView = new RelativeLayout(context);

        mCenterView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mCenterView.setBackgroundColor(getResources().getColor(R.color.debug_orange));
        addView(mCenterView);


        mInterpolator = new AccelerateDecelerateInterpolator();
        addDemoContent((RelativeLayout) mCenterView);
        addDemoContent((RelativeLayout) mLeftView);
        addDemoContent((RelativeLayout) mRightView);


        this.setOnTouchListener(new OnTouchListener() {
            public float mInitialX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int actionMasked = event.getActionMasked();

                switch (actionMasked) {
                    case MotionEvent.ACTION_DOWN:
                        mInitialX = event.getX();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getX() - mInitialX;
                        float x = mLeft + newX;
                        centerViewUpdated(x);
                        break;

                    case MotionEvent.ACTION_UP:
                        centerViewUpdateFinished();
                        break;
                }

                return true;
            }
        });
    }

    private void addDemoContent(RelativeLayout container) {
        String text = "Lorem Ipsum Dolor Sit ";
        for (int i = 0; i < 7; i++) {
            text = text + text;
        }
        TextView tv = new TextView(getContext());
        tv.setText(text);

        container.addView(tv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

    }

    private void centerViewUpdateFinished() {
        switch (mSnap) {
            case CENTER:
                mCenterView.animate().translationX(0);
                mLeftView.animate().translationX(0).alpha(mBaseAlpha);
                mRightView.animate().translationX(0).alpha(mBaseAlpha);
                break;
            case RIGHT:
                mCenterView.animate().translationX(mRight);
                mLeftView.animate().translationX(mRight).alpha(1.0f);
                mRightView.animate().translationX(mRight).alpha(mBaseAlpha);
                break;
            case LEFT:
                mCenterView.animate().translationX(mLeft - mRight);
                mLeftView.animate().translationX(mLeft - mRight).alpha(mBaseAlpha);
                mRightView.animate().translationX(mLeft - mRight).alpha(1.0f);
                break;
        }

    }

    private void centerViewUpdated(float x) {
        boolean toRight = x > 0;
        mCenterView.setX(x);
        float percentage = mInterpolator.getInterpolation(Math.abs(x) / mRight);
        mBaseAlpha = 0.5f;
        float alpha = mBaseAlpha + (percentage * (1 - mBaseAlpha));
        if (toRight) {
            float leftPosition = (1 - percentage) * (mLeft - mRight);
            mLeftView.setX(leftPosition);
            mLeftView.setAlpha(alpha);
            mRightView.setX(mRight + percentage * (mRight));
        } else {
            mLeftView.setX(percentage * (mLeft - mRight) - mRight);
            float rightPosition = mRight - (percentage) * (mRight);
            mRightView.setX(rightPosition);
            mRightView.setAlpha(alpha);
        }

        if (percentage > .4f) {
            if (toRight) {
                mSnap = DrawerSnap.RIGHT;
            } else {
                mSnap = DrawerSnap.LEFT;
            }
        } else {
            mSnap = DrawerSnap.CENTER;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Bog.v(Bog.Category.UI, "Layouting DrawerView");
        mLeft = l;
        mRight = r;
//        int i = r / 2;

        mBackgroundView.layout(l, t, r, b);
        mCenterView.layout(l, t, r, b);
        mLeftView.layout(-r, t, l, b);
        mRightView.layout(r, t, 2 * r, b);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }


    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
