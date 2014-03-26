package ch.dreipol.android.blinq.ui.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
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
        ImageView rightImageView = new ImageView(getContext());
        TextView textView = new TextView(getContext());

        addView(leftImageView);
        addView(textView);
        addView(rightImageView);

        textView.setText(a.getString(R.styleable.SettingsButton_buttonText));
        leftImageView.setImageDrawable(a.getDrawable(R.styleable.SettingsButton_leftImage));
        rightImageView.setImageDrawable(a.getDrawable(R.styleable.SettingsButton_rightImage));

        a.recycle();

    }
}
