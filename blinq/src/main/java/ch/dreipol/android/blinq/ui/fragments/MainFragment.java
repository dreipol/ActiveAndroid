package ch.dreipol.android.blinq.ui.fragments;

import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.util.Bog;

/**
 * Created by phil on 21.03.14.
 */

public class MainFragment extends Fragment {


    private IMainFragmentListener mMainFragmentListener;
    private TextView mLocationDetailsTextView;
    private Geocoder mGeoCoder;

    public void setMainFragmentListener(IMainFragmentListener mainFragmentListener) {
        mMainFragmentListener = mainFragmentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_center_container, container, false);


        v.findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainFragmentListener != null) {
                    mMainFragmentListener.onSettingsClick();
                }

            }
        });
        v.findViewById(R.id.match_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainFragmentListener != null) {
                    mMainFragmentListener.onMatchesClick();
                }
            }
        });

        mLocationDetailsTextView = (TextView)v.findViewById(R.id.location_details_textview);

        Location currentLocation = AppService.getInstance().getLocationService().getCurrentLocation();
        mGeoCoder = new Geocoder(getActivity(), Locale.getDefault());

        String name = getLocationName(currentLocation);

        return v;
    }

    private String getLocationName(Location currentLocation) {
        String result = "";
        if(currentLocation!=null)
        {

            try {
                List<Address> fromLocation = mGeoCoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                Address address = fromLocation.get(0);
                result = address.toString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result
                ;
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Bog.d(Bog.Category.SYSTEM, "Location Updates Google Play services is available.");
            return true;
            // Google Play services was not available for some reason
        }
        return true;
    }

    public interface IMainFragmentListener {

        public void onSettingsClick();

        public void onMatchesClick();

    }
}
