package ch.dreipol.android.blinq.ui.viewgroups;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.util.Bog;

/**
 * Created by phil on 19.03.14.
 */
public class BlinqDrawerLayout extends ViewGroup {

    private final RelativeLayout mLeftView;
    private final RelativeLayout mRightView;
    private final RelativeLayout mCenterView;
    private Interpolator mInterpolator;
    private View mBackgroundView;
    private FrameLayout mLeftViewContainer;
    private FrameLayout mRightViewContainer;
    private FrameLayout mCenterViewContainer;
    private int mLeft;
    private int mRight;
    private DrawerSnap mSnap;
    private float mBaseAlpha;
    private float mXTranslation;
    private int mBaseRotation;
    private float mBaseScale;


    public BlinqDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundView = new View(context);
        mBackgroundView.setBackgroundColor(getResources().getColor(R.color.blinq_black));
        addView(mBackgroundView);

        mLeftViewContainer = new FrameLayout(context);
        mLeftViewContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mLeftViewContainer.setBackgroundColor(getResources().getColor(R.color.debug_blue));
        addView(mLeftViewContainer);

        mRightViewContainer = new FrameLayout(context);
        mRightViewContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRightViewContainer.setBackgroundColor(getResources().getColor(R.color.debug_green));
        addView(mRightViewContainer);

        mCenterViewContainer = new FrameLayout(context);
        mCenterViewContainer.setAlpha(0.5f);
        mCenterViewContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mCenterViewContainer.setBackgroundColor(getResources().getColor(R.color.debug_orange));
        addView(mCenterViewContainer);







        mCenterView =  new RelativeLayout(context);
        mCenterViewContainer.addView(mCenterView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mLeftView = new RelativeLayout(context);
        FrameLayout.LayoutParams leftViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        leftViewParams.setMargins(0,0,100,0);
        mLeftViewContainer.addView(mLeftView, leftViewParams);

        mRightView = new RelativeLayout(context);
        FrameLayout.LayoutParams rightViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        rightViewParams.setMargins(100, 0, 0, 0);
        mRightViewContainer.addView(mRightView, rightViewParams);

        mInterpolator = new AccelerateDecelerateInterpolator();
        addDemoContent(mLeftView);
        addDemoContent(mCenterView);
        addDemoContent(mRightView);

        mXTranslation = 0;
        this.setOnTouchListener(new OnTouchListener() {
            public float mInitialX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float eventX = event.getX();
                boolean xBigger = eventX > mCenterViewContainer.getX();
                boolean xWidthBigger = eventX < mCenterViewContainer.getX() + mRight;

                int actionMasked = event.getActionMasked();

                if (!(xBigger && xWidthBigger) && actionMasked == MotionEvent.ACTION_DOWN) {
                    return false;
                }



                float translation = eventX - mInitialX;

                switch (actionMasked) {
                    case MotionEvent.ACTION_DOWN:
                        mInitialX = eventX - mXTranslation;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        centerViewUpdated(translation);
                        break;
                    case MotionEvent.ACTION_UP:
                        centerViewUpdateFinished();
                        mXTranslation = translation;
                        break;
                }
                return true;
            }
        });
    }


    private void addDemoContent(RelativeLayout container) {
        String text = "Lorem Ipsum Dolor Sit ";
        for (int i = 0; i < 1; i++) {
            text = text + text;
        }
        Button tv = new Button(getContext());
        tv.setText(text);

        container.addView(tv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

    }

    private void centerViewUpdateFinished() {
        float centerTranslation = 0;
        switch (mSnap) {
            case CENTER:
                mLeftViewContainer.animate().translationX(0).scaleX(1).scaleY(1).rotationY(mBaseRotation).alpha(mBaseAlpha);
                mRightViewContainer.animate().translationX(0).scaleX(1).scaleY(1).rotationY(-mBaseRotation).alpha(mBaseAlpha);
                break;
            case RIGHT:
                centerTranslation = getScaledRight();
                mLeftViewContainer.animate().translationX(centerTranslation-100).scaleX(1).scaleY(1).rotationY(0).alpha(1.0f);
                mRightViewContainer.animate().translationX(centerTranslation).scaleX(1).scaleY(1).rotationY(-mBaseRotation).alpha(mBaseAlpha);
                centerTranslation = centerTranslation - 100;
                break;
            case LEFT:
                centerTranslation = mLeft - getScaledRight();
                mLeftViewContainer.animate().translationX(centerTranslation).scaleX(1).scaleY(1).rotationY(mBaseRotation).alpha(mBaseAlpha);
                mRightViewContainer.animate().translationX(centerTranslation+100).scaleX(1).scaleY(1).rotationY(0).alpha(1.0f);
                centerTranslation = centerTranslation + 100;
                break;
        }
        mCenterViewContainer.animate().translationX(centerTranslation);
        final float finalCenterTranslation = centerTranslation;
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                mXTranslation = finalCenterTranslation;
            }
        }, 10);

    }


    private void centerViewUpdated(float x) {
        boolean toRight = x > 0;
        float v = ((float) mRight- 100.0f) / (float) mRight;
        Bog.v(Bog.Category.UI, "v: " + v);
        mCenterViewContainer.setTranslationX(x * v);

        float percentage = Math.abs(x) / (getScaledRight() + 100);
        mBaseAlpha = 0.3f;
        mBaseRotation = 0;
        mBaseScale = 1;

        float alpha = mBaseAlpha + (percentage * (1 - mBaseAlpha));
        float rotation = (1-percentage)*mBaseRotation;
        float scale = mBaseScale + (percentage * (1 - mBaseScale));

        float rightPosition = getRightViewX(percentage);
        float leftPosition = getLeftViewX(percentage);
        if (toRight) {
            mLeftViewContainer.setX(leftPosition);
            mLeftViewContainer.setAlpha(alpha);
            mRightViewContainer.setX(mRight + percentage * (mRight));

        } else {
            mLeftViewContainer.setX(percentage * (mLeft - mRight) - mRight);


            mRightViewContainer.setX(rightPosition);
            mRightViewContainer.setAlpha(alpha);
        }
        mRightViewContainer.setRotationY(-rotation);
        mRightViewContainer.setScaleX(scale);
        mRightViewContainer.setScaleY(scale);
        mLeftViewContainer.setRotationY(rotation);
        mLeftViewContainer.setScaleX(scale);
        mLeftViewContainer.setScaleY(scale);

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

    private float getScaledRight() {
        return mRight * 1;
    }

    private float getLeftViewX(float percentage) {
        return (1 - percentage) * (mLeft - getScaledRight()+100);
    }

    private float getRightViewX(float percentage) {
        float right = getScaledRight();
        return right - (percentage) * right;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Bog.v(Bog.Category.UI, "Layouting DrawerView");
        mLeft = l;
        mRight = r;
//        int i = r / 2;

        mBackgroundView.layout(l, t, r, b);
        mCenterViewContainer.layout(l, t, r, b);
        mLeftViewContainer.layout(-r + 100, t, l, b);
        mRightViewContainer.layout(r, t, r + (r - 100), b);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                if(child== mLeftViewContainer || child == mRightViewContainer){
                    int s = MeasureSpec.getSize(widthMeasureSpec);
                    child.measure(MeasureSpec.makeMeasureSpec(s - 100, MeasureSpec.EXACTLY), heightMeasureSpec);
                }else{
                    child.measure(widthMeasureSpec, heightMeasureSpec);
                }
            }
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }


    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
