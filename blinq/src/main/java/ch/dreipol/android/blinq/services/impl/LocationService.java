package ch.dreipol.android.blinq.services.impl;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ILocationService;

/**
 * Created by phil on 22.03.14.
 */
public class LocationService extends BaseService implements ILocationService, LocationListener {


    public static final int MILLISECS = 1000;
    private LocationManager mLocationManager;

    @Override
    public void setup(AppService appService) {
        super.setup(appService);
        mLocationManager = (LocationManager) getService().getContext().getSystemService(Context.LOCATION_SERVICE);
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MILLISECS * 60 * 1, 100, this);
    }

    @Override
    public Location getCurrentLocation() {
        return mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location location) {
        notifyObservers();
        final LocationListener listener = this;
        mLocationManager.removeUpdates(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestLocationUpdates();
            }
        }, MILLISECS * 60 * 5);
    }


    @Override
    public void dispose() {
        super.dispose();
        mLocationManager.removeUpdates(this);
        mLocationManager = null;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        notifyObservers();
    }

    @Override
    public void onProviderEnabled(String provider) {
        notifyObservers();
    }

    @Override
    public void onProviderDisabled(String provider) {
        notifyObservers();
    }
}