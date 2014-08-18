package ch.dreipol.android.blinq.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ILocationService;
import ch.dreipol.android.blinq.services.impl.LocationService;
import ch.dreipol.android.blinq.ui.headers.IHeaderViewConfiguration;
import rx.functions.Action1;

/**
 * Created by phil on 21.03.14.
 */

public class MainFragment extends Fragment implements IHeaderConfigurationProvider {


    private TextView mLocationDetailsTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_center_container, container, false);


        mLocationDetailsTextView = (TextView) v.findViewById(R.id.location_details_textview);

        ILocationService locationService = AppService.getInstance().getLocationService();

//        TODO: check out the threads.
        locationService.subscribeToLocation().subscribe(new Action1<LocationService.LocationInformation>() {
            @Override
            public void call(LocationService.LocationInformation locationInformation) {
                if (locationInformation.status == LocationService.LocationStatus.VALID) {
                    mLocationDetailsTextView.setText(locationInformation.locationName);
                } else {
                    mLocationDetailsTextView.setText("Location not available");
                }
            }
        });


        return v;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IHeaderViewConfiguration getHeaderConfiguration() {
        return new IHeaderViewConfiguration() {
            @Override
            public boolean showTitle() {
                return false;
            }

            @Override
            public String getTitle() {
                return null;
            }
        };
    }
}
