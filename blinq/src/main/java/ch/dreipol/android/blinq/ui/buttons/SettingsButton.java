package ch.dreipol.android.blinq.ui.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;

/**
 * Created by phil on 26.03.14.
 */
public class SettingsButton extends RelativeLayout {
    public SettingsButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = AppService.getInstance().getRuntimeService().getLayoutInflator(context);
        inflater.inflate(R.layout.settings_button_layout, this, true);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingsButton);

        ImageView leftImageView = (ImageView) findViewById(R.id.icon_view);
        TextView textView = (TextView) findViewById(R.id.text_view);

        textView.setText(a.getString(R.styleable.SettingsButton_buttonText));
        textView.setTextColor(a.getColor(R.styleable.SettingsButton_buttonTextColor, 1));

        leftImageView.setImageDrawable(a.getDrawable(R.styleable.SettingsButton_leftImage));
        a.recycle();

    }


}
