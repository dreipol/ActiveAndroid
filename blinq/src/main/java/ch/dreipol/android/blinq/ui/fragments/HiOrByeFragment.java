package ch.dreipol.android.blinq.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.ui.HiOrByeView;
import rx.functions.Action1;

/**
 * Created by phil on 05/09/14.
 */
public class HiOrByeFragment extends BlinqFragment {

    @Override
    public void onStart() {
        super.onStart();

        mViewSubject.subscribe(new Action1<View>() {
            @Override
            public void call(View view) {

                RelativeLayout container= (RelativeLayout) view.findViewById(R.id.hi_or_bye_container);

                container.requestDisallowInterceptTouchEvent(true);

                container.addView(new HiOrByeView(container.getContext(), null));

            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_hi_or_bye;
    }





}
