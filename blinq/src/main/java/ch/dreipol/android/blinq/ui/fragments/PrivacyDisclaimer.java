package ch.dreipol.android.blinq.ui.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.dreipol.android.blinq.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class PrivacyDisclaimer extends Fragment {


    public PrivacyDisclaimer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View targetView = inflater.inflate(R.layout.fragment_privacy_disclaimer, container, false);
        return targetView;
    }


}
