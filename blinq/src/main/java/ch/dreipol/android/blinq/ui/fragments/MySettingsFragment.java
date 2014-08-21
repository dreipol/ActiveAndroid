package ch.dreipol.android.blinq.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IAccountService;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.SearchSettings;
import ch.dreipol.android.blinq.util.Bog;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;

/**
 * Created by phil on 24.04.14.
 */
public class MySettingsFragment extends Fragment {


    private Subscription mSearchSettingsSubscription;
    private BehaviorSubject<View> mUIState;
    private BehaviorSubject<LoadingInfo<SearchSettings>> mSearchSettingsOservable;
    private SearchSettings mSearchSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IAccountService accountService = AppService.getInstance().getAccountService();

        mUIState = BehaviorSubject.create();
        mSearchSettingsOservable = BehaviorSubject.create();

        mSearchSettingsSubscription = accountService.getSearchSettings().subscribe(new Action1<SearchSettings>() {
            @Override
            public void call(SearchSettings searchSettings) {
                LoadingInfo<SearchSettings> result = new LoadingInfo<SearchSettings>(LoadingState.LOADED);
                result.setData(searchSettings);
                mSearchSettingsOservable.onNext(result);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mSearchSettingsOservable.onNext(new LoadingInfo(LoadingState.ERROR));
            }
        });


        Observable.zip(mUIState, mSearchSettingsOservable, new Func2<View, LoadingInfo<SearchSettings>, LoadingInfo<SearchSettings>>() {
            @Override
            public LoadingInfo<SearchSettings> call(View view, LoadingInfo<SearchSettings> loadingInfo) {
                loadingInfo.setViewContainer(view);
                return loadingInfo;
            }
        }).subscribe(new Action1<LoadingInfo<SearchSettings>>() {
            @Override
            public void call(LoadingInfo<SearchSettings> loadingInfo) {
                View v =  loadingInfo.getViewContainer();
                mSearchSettings = loadingInfo.getData();
                configureDistanceControls(v);
                configureAgeControls(v);
                configureInterestedControls(v);
                configureVibrationControls(v);


            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSearchSettingsSubscription.unsubscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_settings, container, false);
        mUIState.onNext(v);
        return v;
    }

    private void configureVibrationControls(View v) {
        final ToggleButton offButton = (ToggleButton) v.findViewById(R.id.vibration_off);
        final ToggleButton onButton = (ToggleButton) v.findViewById(R.id.vibration_on);

        offButton.setChecked(!mSearchSettings.getVibrate());
        onButton.setChecked(mSearchSettings.getVibrate());

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButton.setChecked(false);
                mSearchSettings.setVibrate(false);
                updateSettings();
            }
        });

        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offButton.setChecked(false);
                mSearchSettings.setVibrate(true);
                updateSettings();
            }
        });
    }

    private void configureInterestedControls(View v) {
        final ToggleButton maleButton = (ToggleButton) v.findViewById(R.id.interest_males);
        final ToggleButton femaleButton = (ToggleButton) v.findViewById(R.id.interest_females);
        final ToggleButton bothButton = (ToggleButton) v.findViewById(R.id.interest_both);

        ToggleButton toBeEnabled = null;

        switch (mSearchSettings.getInterestedIn()){
            case MALE:
                toBeEnabled = maleButton;
                break;
            case FEMALE:
                toBeEnabled = femaleButton;
                break;
            case BOTH:
                toBeEnabled = bothButton;
                break;
        }

        toBeEnabled.setChecked(true);

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButton.setChecked(false);
                bothButton.setChecked(false);
                mSearchSettings.setInterestedIn(SearchSettings.SearchInterest.MALE);
                updateSettings();
            }
        });
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButton.setChecked(false);
                bothButton.setChecked(false);
                mSearchSettings.setInterestedIn(SearchSettings.SearchInterest.FEMALE);
                updateSettings();
        }

        });
        bothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButton.setChecked(false);
                maleButton.setChecked(false);
                mSearchSettings.setInterestedIn(SearchSettings.SearchInterest.BOTH);
                updateSettings();
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
                mSearchSettings.setTo(val);
                updateSettings();
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
                mSearchSettings.setTo(val);
                updateSettings();

            }
        });

        fromBar.setProgress(mSearchSettings.getFrom()-18);
        toBar.setProgress(mSearchSettings.getTo()-18);

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
                mSearchSettings.setDistance(progress);
                updateSettings();
            }
        });

        distanceBar.setProgress(mSearchSettings.getDistance());
    }

    private void updateSettings() {
        AppService.getInstance().getAccountService().saveSearchSettings(mSearchSettings);
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
