package ch.dreipol.android.blinq.services.impl;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ILocationService;
import rx.Observable;
import rx.subjects.BehaviorSubject;


/**
 * Created by phil on 22.03.14.
 */
public class LocationService extends BaseService implements ILocationService {


    public static final int MILLISECS = 1000;
    private Geocoder mGeoCoder;
    private LocationClient mLocationClient;
    private BehaviorSubject<LocationInformation> mLocationSubject;

    public enum LocationStatus {
        VALID, NO_GEOCODE, INVALID
    }


    public class LocationInformation {
        public LocationStatus status;
        public String locationName;
        public Location mLocation;

        public LocationInformation(LocationStatus status, Location location) {
            this.status = status;
            this.mLocation = location;
            setup();
        }

        private void setup() {
            if (this.status == LocationStatus.VALID) {
                try {

                    List<Address> addresses = LocationService.this.getGeoCoder().getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        Address firstAddress = addresses.get(0);
                        String countryName = firstAddress.getCountryName();
                        String locality = firstAddress.getLocality();
                        this.locationName = String.format("%s, %s", countryName, locality);
                    }
                } catch (IOException e) {
                    this.locationName = "Unable to retrieve a valid Address";
                } catch (NullPointerException nullE) {
                    this.locationName = "Unable to retrieve a valid Address";
                }
            } else {
                this.locationName = "Unknown location";
            }
        }

    }


    @Override
    public void setup(AppService appService) {
        super.setup(appService);

        mLocationSubject = BehaviorSubject.create(new LocationInformation(LocationStatus.INVALID, null));


        final LocationRequest locationRequest = LocationRequest.create().setInterval(MILLISECS * 60).setPriority(LocationRequest.PRIORITY_LOW_POWER);

        mLocationClient = new LocationClient(getService().getContext(), new GooglePlayServicesClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {

                mLocationClient.requestLocationUpdates(locationRequest, new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        mLocationSubject.onNext(new LocationInformation(LocationStatus.VALID, location));
                    }
                });
            }

            @Override
            public void onDisconnected() {
                mLocationSubject.onCompleted();
            }

        }, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                mLocationSubject.onNext(new LocationInformation(LocationStatus.INVALID, null));
            }
        });

        mLocationClient.connect();
    }


    @Override
    public void dispose() {
        super.dispose();
        mLocationClient.disconnect();
    }




    public Geocoder getGeoCoder() {
        if (mGeoCoder == null) {
            mGeoCoder = new Geocoder(getService().getContext(), Locale.getDefault());
        }
        return mGeoCoder;
    }

    @Override
    public Observable<LocationInformation> subscribeToLocation() {
        return mLocationSubject;
    }



}