package ch.dreipol.android.blinq.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.ui.buttons.SettingsButton;

/**
 * Created by phil on 21.03.14.
 */
public class SettingsListFragment extends Fragment {

    public void setSettingsListListener(ISettingsListListener mSettingsListListener) {
        this.mSettingsListListener = mSettingsListListener;
    }

    private ISettingsListListener mSettingsListListener;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings_list, container, false);



        getSettingsButton(v, R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsListListener.helpTapped();
            }
        });

        getSettingsButton(v, R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsListListener.settingsTapped();
            }
        });

        getSettingsButton(v, R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsListListener.profileTapped();
            }
        });

        getSettingsButton(v, R.id.matches).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsListListener.matchesTapped();
            }
        });

        getSettingsButton(v, R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingsListListener.homeTapped();
            }
        });

        return v;
    }

    private SettingsButton getSettingsButton(View v, int resource) {
        return (SettingsButton) v.findViewById(resource);
    }
}
