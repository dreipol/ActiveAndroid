package ch.dreipol.android.blinq.ui.buttons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;

/**
 * Created by phil on 26.03.14.
 */
public class SettingsButton extends RelativeLayout {
    private Drawable mDownBackground;
    private Drawable mUpBackground;

    public SettingsButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = AppService.getInstance().getRuntimeService().getLayoutInflator(context);
        inflater.inflate(R.layout.ui_settings_button, this, true);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SettingsButton);

        ImageView leftImageView = (ImageView) findViewById(R.id.icon_view);
        TextView textView = (TextView) findViewById(R.id.text_view);

        textView.setText(a.getString(R.styleable.SettingsButton_buttonText));
        textView.setTextColor(a.getColor(R.styleable.SettingsButton_buttonTextColor, 1));





        setBackgroundResource(R.drawable.listenelement_standard);

        leftImageView.setImageDrawable(a.getDrawable(R.styleable.SettingsButton_leftImage));
        a.recycle();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int actionMasked = event.getActionMasked();
                switch (actionMasked){
                    case MotionEvent.ACTION_DOWN:
                        setBackgroundResource(R.drawable.listenelement_selected);
                        break;

                    case MotionEvent.ACTION_UP:
                        setBackgroundResource(R.drawable.listenelement_standard);
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        setBackgroundResource(R.drawable.listenelement_standard);
                        break;
                }

                return false;
            }
        });

    }


}
