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
public class TutorialFragment extends Fragment {


    public static final String IMAGE = "IMG";
    public static final String TEXT = "TXT";

    public TutorialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }


}
