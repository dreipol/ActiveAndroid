package ch.dreipol.android.blinq.ui;

import android.view.View;

/**
 * Created by phil on 06/09/14.
 */
public interface HiOrByeCard {

    public View getView();
    public void hi();
    public void bye();

    boolean isInteractive();
}
