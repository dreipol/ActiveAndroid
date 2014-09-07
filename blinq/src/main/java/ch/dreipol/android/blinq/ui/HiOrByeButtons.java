package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.util.StaticResources;

/**
 * Created by phil on 07/09/14.
 */
public class HiOrByeButtons extends RelativeLayout {
    private final View mHiButton;
    private final View mByeButton;

    public HiOrByeButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflator = StaticResources.getLayoutInflator(context);
        layoutInflator.inflate(R.layout.ui_hi_or_bye_buttons, this, true);

        mHiButton = findViewById(R.id.hi_button);
        mByeButton = findViewById(R.id.bye_button);

    }

    public View getHiButton() {
        return mHiButton;
    }

    public View getByeButton() {
        return mByeButton;
    }
}
