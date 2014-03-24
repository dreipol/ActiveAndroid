package ch.dreipol.android.blinq.services;

import android.location.Location;

import java.util.Observer;

/**
 * Created by phil on 22.03.14.
 */
public interface ILocationService extends IService{



    boolean available();

    void addLocationObserver(Observer observer);

    void removeLocationObserver(Observer observer);

    Location getCurrentLocation();

    boolean hasLocation();

    String getCurrentLocationTitle();
}
