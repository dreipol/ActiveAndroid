package ch.dreipol.android.blinq.ui.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 26.03.14.
 */
public class SettingsButton extends FrameLayout {
    public SettingsButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingsButton);


        ImageView leftImageView = new ImageView(getContext());
//
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.CENTER_IN_PARENT);

        RelativeLayout.LayoutParams pT = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pT.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.CENTER_IN_PARENT);

        TextView textView = new TextView(getContext());


        textView.setText(a.getString(R.styleable.SettingsButton_buttonText));
        textView.setTextColor(a.getColor(R.styleable.SettingsButton_buttonTextColor, 1));
        leftImageView.setImageDrawable(a.getDrawable(R.styleable.SettingsButton_leftImage));


        addView(leftImageView,p);
        addView(textView, pT);

        a.recycle();

    }
}
