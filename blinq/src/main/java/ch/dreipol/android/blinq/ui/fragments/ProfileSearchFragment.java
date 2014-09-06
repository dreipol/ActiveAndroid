package ch.dreipol.android.blinq.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.ILocationService;
import ch.dreipol.android.blinq.services.impl.LocationService;
import ch.dreipol.android.blinq.ui.activities.MainActivity;
import ch.dreipol.android.blinq.ui.headers.IHeaderViewConfiguration;
import ch.dreipol.android.blinq.ui.viewgroups.DrawerPosition;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by phil on 21.03.14.
 */

public class ProfileSearchFragment extends BlinqFragment implements IHeaderConfigurationProvider {


    private TextView mLocationDetailsTextView;
    private Subscription mLocationSubscription;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewSubject.subscribe(new Action1<View>() {
            @Override
            public void call(View view) {
                mLocationDetailsTextView = (TextView) view.findViewById(R.id.location_details_textview);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        subscribeToLocationSignal();
    }

    @Override
    public void setPosition(DrawerPosition newPosition) {
        super.setPosition(newPosition);
        switch (newPosition) {
            case CENTER:
                subscribeToLocationSignal();
                break;
            case LEFT:
            case RIGHT:
                unsubscribeToLoactionSignal();
                break;
        }
    }

    private void unsubscribeToLoactionSignal() {
        if (mLocationSubscription != null) {
            mLocationSubscription.unsubscribe();
            mLocationSubscription = null;
        }
    }

    private void subscribeToLocationSignal() {
        ILocationService locationService = AppService.getInstance().getLocationService();
        mLocationSubscription = locationService.subscribeToLocation().subscribe(new Action1<LocationService.LocationInformation>() {
            @Override
            public void call(LocationService.LocationInformation locationInformation) {
                if (locationInformation.status == LocationService.LocationStatus.VALID) {
                    mLocationDetailsTextView.setText(locationInformation.locationName);
                } else {
                    mLocationDetailsTextView.setText("Location not available");
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        unsubscribeToLoactionSignal();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_center_container;
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

            @Override
            public boolean hasIcon() {
                return true;
            }

            @Override
            public int getIconDrawable() {
                return R.drawable.icon_chat_standard;

            }

            @Override
            public void iconTapped() {
                FragmentActivity activity = getActivity();
                if(activity instanceof MainActivity){
                    MainActivity ac = (MainActivity) activity;
                    ac.matchesTapped();
                }
            }
        };
    }
}
