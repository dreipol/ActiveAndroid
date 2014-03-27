package ch.dreipol.android.blinq.ui.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 26.03.14.
 */
public class SettingsButton extends RelativeLayout {
    public SettingsButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingsButton);

        ImageView leftImageView = new ImageView(getContext());

        LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        p.addRule(ALIGN_PARENT_LEFT, CENTER_IN_PARENT);

        LayoutParams pT = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pT.addRule(CENTER_IN_PARENT, CENTER_IN_PARENT);


        TextView textView = new TextView(getContext());


        textView.setText(a.getString(R.styleable.SettingsButton_buttonText));
        textView.setTextColor(a.getColor(R.styleable.SettingsButton_buttonTextColor, 1));
        leftImageView.setImageDrawable(a.getDrawable(R.styleable.SettingsButton_leftImage));


        addView(leftImageView,p);
        addView(textView, pT);

        a.recycle();

    }
}
