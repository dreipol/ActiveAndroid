package ch.dreipol.android.blinq.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ILocationService;
import ch.dreipol.android.blinq.services.impl.LocationService;
import ch.dreipol.android.blinq.util.Bog;
import rx.functions.Action1;

/**
 * Created by phil on 21.03.14.
 */

public class MainFragment extends Fragment {


    private IMainFragmentListener mMainFragmentListener;
    private TextView mLocationDetailsTextView;

    public void setMainFragmentListener(IMainFragmentListener mainFragmentListener) {
        mMainFragmentListener = mainFragmentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_center_container, container, false);


//        v.findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mMainFragmentListener != null) {
//                    mMainFragmentListener.onSettingsClick();
//                }
//
//            }
//        });
//        v.findViewById(R.id.match_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mMainFragmentListener != null) {
//                    mMainFragmentListener.onMatchesClick();
//                }
//            }
//        });
        mLocationDetailsTextView = (TextView) v.findViewById(R.id.location_details_textview);
        v.findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bog.d(Bog.Category.UI, "Test BUtton Tappeditapped");
            }
        });

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

    public interface IMainFragmentListener {

        public void onSettingsClick();

        public void onMatchesClick();

    }
}
