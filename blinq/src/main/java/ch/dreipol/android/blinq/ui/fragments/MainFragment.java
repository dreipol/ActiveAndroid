package ch.dreipol.android.blinq.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ILocationService;

/**
 * Created by phil on 21.03.14.
 */

public class MainFragment extends Fragment implements Observer {


    private IMainFragmentListener mMainFragmentListener;
    private TextView mLocationDetailsTextView;

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
        mLocationDetailsTextView = (TextView) v.findViewById(R.id.location_details_textview);


        ILocationService locationService = getLocationService();
        if (locationService.available()) {

            updateLocation();
            locationService.addLocationObserver(this);
        }else{
            mLocationDetailsTextView.setText("Location not available");
        }


        return v;
    }

    private void updateLocation() {
        ILocationService locationService = getLocationService();
        if (locationService.available()) {
            mLocationDetailsTextView.setText(locationService.getCurrentLocationTitle());
        }
    }


    private ILocationService getLocationService() {
        return AppService.getInstance().getLocationService();
    }


    @Override
    public void update(Observable observable, Object data) {
        updateLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLocationService().removeLocationObserver(this);
    }

    public interface IMainFragmentListener {

        public void onSettingsClick();

        public void onMatchesClick();

    }
}
