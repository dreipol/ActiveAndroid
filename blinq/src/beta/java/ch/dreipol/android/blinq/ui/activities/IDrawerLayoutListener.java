package ch.dreipol.android.blinq.ui.activities;

import ch.dreipol.android.blinq.ui.viewgroups.DrawerPosition;

/**
 * Created by phil on 21.03.14.
 */
public interface IDrawerLayoutListener {
    public void beginOrContinueMovement();
    public void finishMovementOnPosition(DrawerPosition newPosition);
}
