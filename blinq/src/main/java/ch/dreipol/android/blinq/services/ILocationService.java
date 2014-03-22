package ch.dreipol.android.blinq.services;

import android.location.Location;

/**
 * Created by phil on 22.03.14.
 */
public interface ILocationService extends IService{

    Location getCurrentLocation();

}
