package ch.dreipol.android.blinq.services;

import ch.dreipol.android.blinq.services.model.Profile;

/**
 * Created by melbic on 10/07/14.
 */
public interface ISwarmIterator {
    public void hi();

    public void bye();

    public void setProfileListener(IProfileListener listener);

}
