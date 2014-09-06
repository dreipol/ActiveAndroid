package ch.dreipol.android.blinq.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.blinq.util.StaticResources;

/**
 * Created by phil on 05/09/14.
 */
public class HiOrByeView extends ViewGroup {
    private RelativeLayout mFirstViewContainer;
    private RelativeLayout mSecondViewContainer;
    private int mBorderMargin;
    private int mRight;
    private float mInitialX;
    private float mXTranslation;

    public enum HIORBYE{
        HI, BYE, INIT, BACK
    }

    private float mBaseScale;
    private float mBaseAlpha;
    private float mEventX;
    private CardProvider mCardProvider;
    private HiOrByeCard mFirstCard;
    private HiOrByeCard mSecondCard;

    public HiOrByeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBaseScale = 0.75f;
        mBaseAlpha = 0.1f;

        mBorderMargin = StaticResources.convertDisplayPointsToPixel(getContext(), 0);
        mSecondViewContainer = new RelativeLayout(context, null);
        addView(mSecondViewContainer);

        mFirstViewContainer = new RelativeLayout(context, null);
        addView(mFirstViewContainer);

        setBackgroundColor(getResources().getColor(R.color.blinq_black));



        mInitialX = 0;
        mXTranslation = 0;

        this.setOnTouchListener(new OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {


                mEventX = event.getX();


                int actionMasked = event.getActionMasked();


                float translation = mEventX - mInitialX;

                switch (actionMasked) {
                    case MotionEvent.ACTION_DOWN:
                        mInitialX = mEventX - mXTranslation;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updateViewWithTranslation(translation);
                        break;
                    case MotionEvent.ACTION_UP:
                        finishTransition(translation);
                        mXTranslation = translation;
                        break;
                }

                return false;
            }
        });
    }

    public void setFirstCard(HiOrByeCard hiOrByeCard) {

        mSecondCard = hiOrByeCard;
        mSecondViewContainer.removeAllViews();
        mSecondViewContainer.addView(hiOrByeCard.getView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        animateTransition(HIORBYE.INIT);
    }


    private void finishTransition(float x) {
        float percentage = Math.abs(x) / (getScaledRight() - mBorderMargin);
        HIORBYE transition = null;

        if(percentage<.3f){
            transition =  HIORBYE.BACK;
        }else{
            if (mEventX > getScaledRight() / 2) {
                transition = HIORBYE.HI;

            }else{
                transition = HIORBYE.BYE;
            }
        }

        animateTransition(transition);

    }

    private void animateTransition(HIORBYE transition) {
        float centerTranslation = 0;
        long animationDuration = 300;
        Interpolator bounceInterpolator = new OvershootInterpolator(1.0f);

        Bog.d(Bog.Category.UI, "transition: " + transition);
        switch (transition){
            case BACK:
                mFirstViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(centerTranslation).scaleX(1).scaleY(1).rotationY(0).alpha(1);
                mSecondViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(centerTranslation).scaleX(mBaseScale).scaleY(mBaseScale).rotationY(0).alpha(mBaseAlpha);
                break;

            case BYE:
                int value = -100;
                Bog.d(Bog.Category.UI, "Animating  1123Back: " + value);
                mFirstViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(-mRight).scaleX(1).scaleY(1).rotationY(0).alpha(1);
                mSecondViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(centerTranslation).scaleX(1).scaleY(1).rotationY(0).alpha(1);
                mFirstCard.bye();
                break;
            case HI:
                mFirstViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(mRight).scaleX(1).scaleY(1).rotationY(0).alpha(1);
                mSecondViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(centerTranslation).scaleX(1).scaleY(1).rotationY(0).alpha(1);
                mFirstCard.hi();
                break;

            case INIT:
                mFirstViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationY(getBottom()).scaleX(1).scaleY(1).rotationY(0).alpha(1);
                mSecondViewContainer.animate().setInterpolator(bounceInterpolator).setDuration(animationDuration).translationX(centerTranslation).scaleX(1).scaleY(1).rotationY(0).alpha(1);
                break;

        }
        final float finalCenterTranslation = centerTranslation;

        final HIORBYE finalTransition = transition;
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                mXTranslation = finalCenterTranslation;
            }
        }, 1);

        animate().setDuration(animationDuration).setInterpolator(bounceInterpolator).translationX(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                switch (finalTransition) {
                    case BACK:
                        break;
                    case HI:
                    case INIT:
                    case BYE:

                        RelativeLayout tempGroup = mFirstViewContainer;

                        mFirstViewContainer = mSecondViewContainer;
                        mSecondViewContainer = tempGroup;
                        bringChildToFront(mFirstViewContainer);
                        requestLayout();
                        invalidate();

                        mFirstViewContainer.setTranslationY(0);
                        mSecondViewContainer.setTranslationY(0);
                        mFirstCard = mSecondCard;

                        if(mCardProvider!=null){
                            mSecondCard = mCardProvider.requestCard();
                            mSecondViewContainer.removeAllViews();
                            mSecondViewContainer.addView(mSecondCard.getView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                        }

                        break;
                }
            }
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

            }
        });
    }

    private void updateViewWithTranslation(float x) {
        mFirstViewContainer.setTranslationX(x);


        float percentage = Math.abs(x) / (getScaledRight() - mBorderMargin);


        float scalePercentage  = mBaseScale + percentage*(1.0f-mBaseScale);
        float alphaPercentage  = mBaseAlpha + percentage*(1.0f-mBaseAlpha);

        Bog.d(Bog.Category.UI, "PERCENTAGE: "+ percentage + " x: " + x);


        mSecondViewContainer.setScaleX(scalePercentage);
        mSecondViewContainer.setTranslationX(0);
        mSecondViewContainer.setScaleY(scalePercentage);
        mSecondViewContainer.setAlpha(alphaPercentage);

    }

    private int getScaledRight() {
        return mRight;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mRight = r;
        mFirstViewContainer.layout(l,t,r,b);
        mSecondViewContainer.layout(l,t,r,b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mFirstCard.isInteractive();
    }


    public void setCardProvider(CardProvider provider){
        mCardProvider = provider;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mCardProvider != null) {
            mFirstViewContainer.removeAllViews();
            mFirstCard = mCardProvider.requestCard();
            mFirstViewContainer.addView(mFirstCard.getView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            mSecondViewContainer.removeAllViews();
            mSecondCard = mCardProvider.requestCard();
            mSecondViewContainer.addView(mSecondCard.getView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

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
}
