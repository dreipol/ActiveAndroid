package ch.dreipol.android.blinq.services.impl;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Observer;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ILocationService;
import ch.dreipol.android.blinq.util.Bog;


/**
 * Created by phil on 22.03.14.
 */
public class LocationService extends BaseService implements ILocationService, LocationListener {


    public static final int MILLISECS = 1000;
    private LocationManager mLocationManager;
    private Geocoder mGeoCoder;

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
    public boolean hasLocation() {
        return getCurrentLocation() != null;
    }

    @Override
    public String getCurrentLocationTitle() {
        Location currentLocation = getCurrentLocation();
        try {
            List<Address> addresses = getGeoCoder().getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);

            if (addresses.size() > 0) {
                Address firstAddress = addresses.get(0);
                String countryName = firstAddress.getCountryName();
                String locality = firstAddress.getLocality();
                return String.format("%s, %s", countryName, locality);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException nullE) {
            return "Unable to retrieve a valid Address";
        }
        return "Unknown location";
    }

    @Override
    public boolean available() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getService().getContext());
        if (ConnectionResult.SUCCESS == resultCode) {
            Bog.d(Bog.Category.SYSTEM, "Google Play services are available.");
            return true;
        }
        Bog.d(Bog.Category.SYSTEM, "Google Play services are NOT available.");
        return false;
    }

    @Override
    public void addLocationObserver(Observer observer) {
        addObserver(observer);
    }

    @Override
    public void removeLocationObserver(Observer observer) {
        deleteObserver(observer);
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

    public Geocoder getGeoCoder() {
        if (mGeoCoder == null) {
            mGeoCoder = new Geocoder(getService().getContext(), Locale.getDefault());
        }
        return mGeoCoder;
    }
}