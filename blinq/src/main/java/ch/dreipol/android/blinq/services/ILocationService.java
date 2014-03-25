package ch.dreipol.android.blinq.services;

import android.location.Geocoder;

import ch.dreipol.android.blinq.services.impl.LocationService;
import rx.Observable;

/**
 * Created by phil on 22.03.14.
 */
public interface ILocationService extends IService{

    Observable<LocationService.LocationInformation> subscribeToLocation();
    Geocoder getGeoCoder();

}
