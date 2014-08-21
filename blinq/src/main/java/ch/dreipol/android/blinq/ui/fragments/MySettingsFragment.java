package ch.dreipol.android.blinq.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.util.Bog;

/**
 * Created by phil on 24.04.14.
 */
public class MySettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_settings, container, false);

        configureDistanceControls(v);
        configureAgeControls(v);
        configureInterestedControls(v);
        configureVibrationControls(v);



        return v;
    }

    private void configureVibrationControls(View v) {
        final ToggleButton offButton = (ToggleButton) v.findViewById(R.id.vibration_off);
        final ToggleButton onButton = (ToggleButton) v.findViewById(R.id.vibration_on);

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButton.setChecked(false);
            }
        });

        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offButton.setChecked(false);
            }
        });
    }

    private void configureInterestedControls(View v) {
        final ToggleButton maleButton = (ToggleButton) v.findViewById(R.id.interest_males);
        final ToggleButton femaleButton = (ToggleButton) v.findViewById(R.id.interest_females);
        final ToggleButton bothButton = (ToggleButton) v.findViewById(R.id.interest_both);


        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButton.setChecked(false);
                bothButton.setChecked(false);
            }
        });
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButton.setChecked(false);
                bothButton.setChecked(false);
            }
        });
        bothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButton.setChecked(false);
                maleButton.setChecked(false);
            }
        });
    }

    private void configureAgeControls(View v) {
        final SeekBar fromBar = (SeekBar) v.findViewById(R.id.from_bar);
        final SeekBar toBar = (SeekBar) v.findViewById(R.id.to_bar);

        final TextView fromLabel = (TextView) v.findViewById(R.id.from_label);
        final TextView toLabel = (TextView) v.findViewById(R.id.to_label);


        fromBar.setOnSeekBarChangeListener(new SeekBarAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Integer val = progress + 18;
                fromLabel.setText(val.toString());
                toBar.setProgress(Math.max(toBar.getProgress(), progress));
            }
        });

        toBar.setOnSeekBarChangeListener(new SeekBarAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Integer val = progress + 18;

                String textVal = val.toString();
                if (progress == toBar.getMax()) {
                    textVal = getResources().getString(R.string.infinite);
                }
                toLabel.setText(textVal);
                fromBar.setProgress(Math.min(fromBar.getProgress(), progress));
            }
        });
    }

    private void configureDistanceControls(View v) {
        final SeekBar distanceBar = (SeekBar) v.findViewById(R.id.distance_bar);
        final TextView distanceText = (TextView) v.findViewById(R.id.distance_text);

        distanceBar.setOnSeekBarChangeListener(new SeekBarAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String distanceString = null;

                if (progress == distanceBar.getMax()) {
                    distanceString = getResources().getString(R.string.max_distance);
                } else {
                    distanceString = String.format(getResources().getString(R.string.distance_label), progress);
                }

                distanceText.setText(distanceString);
            }
        });
    }

    public abstract class SeekBarAdapter implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Bog.d(Bog.Category.UI, "Started tracking on seekbar");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Bog.d(Bog.Category.UI, "Stopped tracking on seekbar");
        }
    }
}
