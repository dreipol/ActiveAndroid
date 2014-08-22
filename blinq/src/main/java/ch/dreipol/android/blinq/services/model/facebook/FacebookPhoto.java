package ch.dreipol.android.blinq.services.model.facebook;

import java.util.Collection;

import ch.dreipol.android.blinq.services.model.ILoadable;

/**
 * Created by phil on 22/08/14.
 */
public class FacebookPhoto implements ILoadable{
    String mId;
    Collection<FacebookPhotoSource>mImages;
}
