package ch.dreipol.android.blinq.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.dreipol.android.blinq.R;

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

    public interface IMainFragmentListener {

        public void onSettingsClick();

        public void onMatchesClick();

    }
}
