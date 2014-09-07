package ch.dreipol.android.blinq.ui.viewgroups;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.ui.activities.IDrawerLayoutListener;
import ch.dreipol.android.blinq.ui.headers.HeaderView;
import ch.dreipol.android.blinq.ui.headers.IHeaderViewConfiguration;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.blinq.util.StaticResources;


/**
 * Created by phil on 19.03.14.
 */
public class BlinqDrawerLayout extends ViewGroup {

    private final RelativeLayout mLeftView;
    private final RelativeLayout mRightView;
    private final RelativeLayout mCenterView;
    private final HeaderView mHeaderView;
    private final RelativeLayout mCenterOverlayView;
    private View mBackgroundView;
    private FrameLayout mLeftViewContainer;
    private FrameLayout mRightViewContainer;
    private RelativeLayout mCenterViewContainer;
    private int mLeft;
    private int mRight;
    private DrawerPosition mSnap;
    private float mBaseAlpha;
    private float mXTranslation;
    private int mBaseRotation;
    private float mBaseScale;
    private float mBorderMargin;

    public float mInitialX;
    private IDrawerLayoutListener mDrawerLayoutListener;


    public BlinqDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundView = new View(context);
        mBackgroundView.setBackgroundColor(getResources().getColor(R.color.blinq_black));
        addView(mBackgroundView);

        mLeftViewContainer = new FrameLayout(context);
        mLeftViewContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mLeftViewContainer.setBackgroundColor(getResources().getColor(R.color.blinq_background_almost_black_2f2f2f));
        addView(mLeftViewContainer);

        mRightViewContainer = new FrameLayout(context);
        mRightViewContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRightViewContainer.setBackgroundColor(getResources().getColor(R.color.blinq_background_almost_black_2f2f2f));
        addView(mRightViewContainer);

        mCenterViewContainer = new RelativeLayout(context);
        mCenterViewContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mCenterViewContainer.setBackgroundColor(getResources().getColor(R.color.blinq_white));
        addView(mCenterViewContainer);


        mBorderMargin = StaticResources.convertDisplayPointsToPixel(getContext(), 60);

        mHeaderView = new HeaderView(context, null);
        mHeaderView.setId(StaticResources.generateViewId());


        mCenterViewContainer.addView(mHeaderView, new LayoutParams(LayoutParams.MATCH_PARENT, StaticResources.convertDisplayPointsToPixel(context, 44)));

        mCenterView = new RelativeLayout(context);
        mCenterView.setId(StaticResources.generateViewId());
        RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        centerParams.addRule(RelativeLayout.BELOW, mHeaderView.getId());
        mCenterViewContainer.addView(mCenterView, centerParams);


        mCenterOverlayView = new RelativeLayout(context);
        mCenterOverlayView.setId(StaticResources.generateViewId());
        RelativeLayout.LayoutParams centerOverlayParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        centerOverlayParams.addRule(RelativeLayout.BELOW, mCenterOverlayView.getId());
        mCenterViewContainer.addView(mCenterOverlayView, centerOverlayParams);
        mCenterOverlayView.setBackgroundColor(context.getResources().getColor(R.color.blinq_transparent));
        mCenterOverlayView.setVisibility(GONE);

        mLeftView = new RelativeLayout(context);
        mLeftView.setId(StaticResources.generateViewId());
        FrameLayout.LayoutParams leftViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        leftViewParams.setMargins(0, 0, (int) mBorderMargin, 0);
        mLeftViewContainer.addView(mLeftView, leftViewParams);

        mRightView = new RelativeLayout(context);
        mRightView.setId(StaticResources.generateViewId());
        FrameLayout.LayoutParams rightViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        rightViewParams.setMargins((int) mBorderMargin, 0, 0, 0);
        mRightViewContainer.addView(mRightView, rightViewParams);


        mSnap = DrawerPosition.CENTER;
        mBaseAlpha = 0.3f;
        mBaseRotation = 0;
        mBaseScale = 1.0f;


//        addDemoContent(mLeftView);
//        addDemoContent(mRightView);
//        addDebugControls();

        mXTranslation = 0;

        final GestureDetector detector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                setDrawerPosition(DrawerPosition.CENTER);
                return super.onSingleTapUp(e);

            }
        });

        final GestureDetector swipeDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                boolean toLeft = ;
                Bog.d(Bog.Category.UI, "velocity: " + velocityX);

                if (velocityX < -4000) {
                    if (mSnap == DrawerPosition.RIGHT) {
                        setDrawerPosition(DrawerPosition.CENTER);
                    } else if (mSnap == DrawerPosition.CENTER) {
                        setDrawerPosition(DrawerPosition.LEFT);
                    }

                } else if(velocityX > 4000) {
                    if (mSnap == DrawerPosition.LEFT) {
                        setDrawerPosition(DrawerPosition.CENTER);
                    } else if (mSnap == DrawerPosition.CENTER) {
                        setDrawerPosition(DrawerPosition.RIGHT);
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });


        mCenterViewContainer.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });

        this.setOnTouchListener(new OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeDetector.onTouchEvent(event);
                detector.onTouchEvent(event);


                float eventX = event.getX();


                int actionMasked = event.getActionMasked();

                if (!isTouchInCenter(event) && actionMasked == MotionEvent.ACTION_DOWN) {
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

    private boolean isTouchInCenter(MotionEvent event) {
        float eventX = event.getX();
        boolean xBigger = eventX > mCenterViewContainer.getX();
        boolean xWidthBigger = eventX < mCenterViewContainer.getX() + mRight;
        return (xBigger && xWidthBigger);
    }

    public void setDrawerPosition(DrawerPosition position) {
        mSnap = position;

        centerViewUpdateFinished();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        if (isTouchInCenter(ev) && mCenterOverlayView.getVisibility() == VISIBLE) {
            return true;
        }


        if (isTouchInCenter(ev) && actionMasked != MotionEvent.ACTION_DOWN && actionMasked != MotionEvent.ACTION_UP && actionMasked != MotionEvent.ACTION_MOVE) {
            return true;
        } else {
            mInitialX = ev.getX() - mXTranslation;
            return false;
        }
    }

    private void centerViewUpdateFinished() {
        float centerTranslation = 0;
        long animationDuration = 300;
        Interpolator bounceInterpolator = new OvershootInterpolator(1.0f);
        switch (mSnap) {
            case CENTER:
                mLeftViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(centerTranslation).scaleX(mBaseScale).scaleY(mBaseScale).rotationY(mBaseRotation).alpha(mBaseAlpha);
                mRightViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(centerTranslation).scaleX(mBaseScale).scaleY(mBaseScale).rotationY(-mBaseRotation).alpha(mBaseAlpha);
                break;

            case RIGHT:
                centerTranslation = getScaledRight();
                mLeftViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(mRight / 2).scaleX(1).scaleY(1).rotationY(0).alpha(1.0f);
                mRightViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(centerTranslation).scaleX(mBaseScale).scaleY(mBaseScale).rotationY(-mBaseRotation).alpha(mBaseAlpha);
                centerTranslation = centerTranslation - mBorderMargin;
                break;

            case LEFT:
                centerTranslation = mLeft - getScaledRight();
                mLeftViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(-(mRight / 2)).scaleX(mBaseScale).scaleY(mBaseScale).rotationY(mBaseRotation).alpha(mBaseAlpha);
                mRightViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(-(mRight / 2)).scaleX(1).scaleY(1).rotationY(0).alpha(1.0f);
                centerTranslation = centerTranslation + mBorderMargin;
                break;
        }
        mCenterViewContainer.animate().setDuration(animationDuration).setInterpolator(bounceInterpolator).translationX(centerTranslation).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                switch (mSnap) {
                    case CENTER:
                        mLeftViewContainer.setVisibility(GONE);
                        mRightViewContainer.setVisibility(GONE);
                        mCenterOverlayView.setVisibility(GONE);
                        break;
                    case LEFT:
                        mLeftViewContainer.setVisibility(GONE);
                        mRightViewContainer.setVisibility(VISIBLE);
                        mCenterOverlayView.setVisibility(VISIBLE);
                        break;
                    case RIGHT:
                        mRightViewContainer.setVisibility(GONE);
                        mLeftViewContainer.setVisibility(VISIBLE);
                        mCenterOverlayView.setVisibility(VISIBLE);
                }

                if (mDrawerLayoutListener != null) {
                    mDrawerLayoutListener.finishMovementOnPosition(mSnap);
                }


            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mLeftViewContainer.setVisibility(VISIBLE);
                mRightViewContainer.setVisibility(VISIBLE);

            }
        });
        final float finalCenterTranslation = centerTranslation;

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                mXTranslation = finalCenterTranslation;
            }
        }, 1);

    }


    private void centerViewUpdated(float x) {

        if (mDrawerLayoutListener != null) {
            mDrawerLayoutListener.beginOrContinueMovement();
        }

        boolean toRight = x > 0;
        mCenterViewContainer.setTranslationX(x);

        float percentage = Math.abs(x) / (getScaledRight() - mBorderMargin);


        float alpha = mBaseAlpha + (percentage * (1 - mBaseAlpha));
        float rotation = (1 - percentage) * mBaseRotation;
//        not used for the moment...
//        float scale = mBaseScale + (percentage * (1 - mBaseScale));

        float rightPosition = getRightViewX(percentage);
        float leftPosition = getLeftViewX(percentage);
        float maxPercentage = Math.max(1, percentage);
        float additionalPercentage = 1.0f;
        if (maxPercentage > 1) {


            maxPercentage = 1 + (maxPercentage - 1) / 2;
            additionalPercentage = maxPercentage;
            maxPercentage = 1 + (maxPercentage - 1) / 2;
        } else {
            additionalPercentage = 1;
        }


        if (toRight) {
            mLeftViewContainer.setX(leftPosition - (mBorderMargin * (1 - additionalPercentage)));
            mLeftViewContainer.setAlpha(alpha);
            mLeftViewContainer.setScaleX(maxPercentage);
            mRightViewContainer.setX(mRight + (percentage * mRight));
        } else {
            mLeftViewContainer.setX(percentage * (mLeft - mRight) - mRight);
            mRightViewContainer.setX(rightPosition + (mBorderMargin * (1 - additionalPercentage)));
            mRightViewContainer.setAlpha(alpha);
            mRightViewContainer.setScaleX(maxPercentage);
        }

        mRightViewContainer.setRotationY(-rotation);

        mLeftViewContainer.setRotationY(rotation);

        if (percentage > .3f) {
            if (toRight) {
                mSnap = DrawerPosition.RIGHT;
            } else {
                mSnap = DrawerPosition.LEFT;
            }
        } else {
            mSnap = DrawerPosition.CENTER;
        }
        mLeftViewContainer.setVisibility(VISIBLE);
        mRightViewContainer.setVisibility(VISIBLE);
    }

    private float getScaledRight() {
        return mRight;
    }

    private float getLeftViewX(float percentage) {
        float r2 = getScaledRight() / 2;
        return (percentage) * r2 - r2;
    }

    private float getRightViewX(float percentage) {
        float right = getScaledRight() / 2;
        return right - (percentage) * right;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mLeft = l;
        mRight = r;

        int r2 = r / 2;

        mBackgroundView.layout(l, t, r, b);
        mCenterViewContainer.layout(l, t, r, b);

        mLeftViewContainer.layout(-r2, t, l + r2, b);
        mRightViewContainer.layout(r2, t, r + r2, b);
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


    public void setDrawerLayoutListener(IDrawerLayoutListener iDrawerLayoutListener) {
        mDrawerLayoutListener = iDrawerLayoutListener;
    }

    public int getRightContainer() {
        return mRightView.getId();
    }

    public int getLeftContainer() {
        return mLeftView.getId();
    }

    public int getCenterContainer() {
        return mCenterView.getId();
    }

    public DrawerPosition getDrawerPosition() {
        return mSnap;
    }

    public void updateHeaderConfiguration(IHeaderViewConfiguration headerConfiguration) {
        mHeaderView.updateConfiguration(headerConfiguration);
    }
}
