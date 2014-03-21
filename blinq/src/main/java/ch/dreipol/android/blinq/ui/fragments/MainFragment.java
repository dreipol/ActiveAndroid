package ch.dreipol.android.blinq.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.util.Bog;

/**
 * Created by phil on 21.03.14.
 */

public class MainFragment extends Fragment {


    private IMainFragmentListener mMainFragmentListener;

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


        return v;
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
