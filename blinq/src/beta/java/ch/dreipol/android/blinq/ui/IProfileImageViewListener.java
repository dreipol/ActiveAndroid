package ch.dreipol.android.blinq.ui;

import android.view.View;

/**
 * Created by melbic on 03/09/14.
 */
public interface IProfileImageViewListener {
    View.OnClickListener getAddListener();

    View.OnClickListener getEditListener();

    View.OnClickListener getDeleteListener();
}
