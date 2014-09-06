package ch.dreipol.android.blinq.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.SwarmManager;
import ch.dreipol.android.blinq.ui.CardProvider;
import ch.dreipol.android.blinq.ui.HiOrByeCard;
import ch.dreipol.android.blinq.ui.HiOrByeView;
import ch.dreipol.android.blinq.ui.ProfileSearchView;
import rx.functions.Action1;

/**
 * Created by phil on 05/09/14.
 */
public class HiOrByeFragment extends BlinqFragment implements CardProvider {

    private SwarmManager mSwarmManager;

    @Override
    public void onStart() {
        super.onStart();
        mSwarmManager = new SwarmManager();
        mSwarmManager.reloadSwarm();

        mViewSubject.subscribe(new Action1<View>() {
            @Override
            public void call(View view) {

                RelativeLayout container= (RelativeLayout) view.findViewById(R.id.hi_or_bye_container);

                container.requestDisallowInterceptTouchEvent(true);

                HiOrByeView child = new HiOrByeView(container.getContext(), null);

                child.setCardProvider(HiOrByeFragment.this);


                container.addView(child);

            }
        });
    }

    @Override
    public HiOrByeCard requestCard() {


        return new HiOrByeCard() {
            @Override
            public View getView() {
                return new ProfileSearchView(getActivity(), null);
            }

            @Override
            public void hi() {

            }

            @Override
            public void bye() {

            }
        };
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_hi_or_bye;
    }





}
