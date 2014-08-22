package ch.dreipol.android.blinq.ui.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.util.StaticResources;


/**
 * Created by phil on 25.02.14.
 */
public class FlowLayout extends ViewGroup {

    private final int mHorizontalSpacing;
    private final int mVerticalSpacing;
    private final int mItemSize;

    public FlowLayout(Context context) {
        super(context);
        mHorizontalSpacing = StaticResources.convertDisplayPointsToPixel(context, 10);
        mVerticalSpacing = StaticResources.convertDisplayPointsToPixel(context, 10);

        mItemSize = StaticResources.convertDisplayPointsToPixel(context, 10);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        try {

            mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 10);
            mVerticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 10);
            mItemSize = a.getDimensionPixelSize(R.styleable.FlowLayout_itemSize, 10);
        } finally {
            a.recycle();
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = 0;
        int height = getPaddingTop();


        int currentWidth = getPaddingLeft();
        int currentHeight = 0;
        final int childCount = getChildCount();

        boolean breakline = false;

        List<List<View>> rows = new ArrayList<List<View>>();
        List<View> cols = new ArrayList<View>();


        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            if (breakline || currentWidth + child.getMeasuredWidth() > widthSize) {

                height += currentHeight + mVerticalSpacing;
                currentHeight = 0;
                width = Math.max(width, currentWidth);
                currentWidth = getPaddingLeft();
                rows.add(cols);
                cols = new ArrayList<View>();
            }

            int spacing = mHorizontalSpacing;
            if (layoutParams.spacing > -1) {
                spacing = layoutParams.spacing;
            }
            layoutParams.x = currentWidth;
            layoutParams.y = height;


            currentWidth += mItemSize + spacing;
            currentHeight = Math.max(currentHeight, mItemSize);


            breakline = layoutParams.breakline;
            cols.add(child);
        }
        rows.add(cols);

        width += getPaddingRight();
        height += getPaddingBottom();


        for (List<View> row : rows) {
            int rowSize = row.size() * mItemSize + (row.size() * mHorizontalSpacing);
            int offsetX = (width - rowSize) / 2;
            for (View child : row) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                lp.xOffset = offsetX;
            }
        }

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x + lp.xOffset, lp.y, lp.x + mItemSize + lp.xOffset, lp.y + mItemSize);
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public boolean breakline;
        public int spacing = -1;

        private int xOffset = 0;
        private int x;
        private int y;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FlowLayout_LayoutParams);
            try {
                spacing = a.getDimensionPixelSize(R.styleable.FlowLayout_LayoutParams_layout_spacing, -1);
                breakline = a.getBoolean(R.styleable.FlowLayout_LayoutParams_breakline, false);
            } finally {
                a.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
