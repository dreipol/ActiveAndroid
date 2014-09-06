package ch.dreipol.android.blinq.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IProfileListener;
import ch.dreipol.android.blinq.services.ISwarmIterator;
import ch.dreipol.android.blinq.services.SwarmManager;
import ch.dreipol.android.blinq.services.model.Photo;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.ui.CardProvider;
import ch.dreipol.android.blinq.ui.HiOrByeCard;
import ch.dreipol.android.blinq.ui.HiOrByeView;
import ch.dreipol.android.blinq.ui.ProfileOverviewView;
import ch.dreipol.android.blinq.ui.ProfileSearchView;
import rx.functions.Action1;

/**
 * Created by phil on 05/09/14.
 */
public class HiOrByeFragment extends BlinqFragment implements CardProvider {

    private SwarmManager mSwarmManager;
    private Profile mProfile;
    private ISwarmIterator mSwarmIterator;

    @Override
    public void onStart() {
        super.onStart();
        mSwarmManager = new SwarmManager();


        mSwarmIterator = mSwarmManager.firstIterator();
        mSwarmIterator.setProfileListener(new IProfileListener() {
            @Override
            public void setProfile(Profile profile) {
                mProfile = profile;
//                final Photo photo = profile.getPhotos().get(0);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
            }
        });
        mSwarmManager.reloadSwarm();

        mViewSubject.subscribe(new Action1<View>() {
            @Override
            public void call(View view) {

                RelativeLayout container = (RelativeLayout) view.findViewById(R.id.hi_or_bye_container);

                container.requestDisallowInterceptTouchEvent(true);

                HiOrByeView child = new HiOrByeView(container.getContext(), null);

                child.setCardProvider(HiOrByeFragment.this);


                container.addView(child);

            }
        });
    }

    @Override
    public HiOrByeCard requestCard() {
        mSwarmManager.reloadSwarm();
        if(mProfile!=null){
            return new HiOrByeCard() {
                @Override
                public View getView() {

                    return new ProfileOverviewView(getActivity(), mProfile);
                }

                @Override
                public void hi() {
                    mSwarmIterator.hi();
                }

                @Override
                public void bye() {
                    mSwarmIterator.bye();
                }
            };
        }else{
            return new SearchCard();
        }


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_hi_or_bye;
    }


    class SearchCard implements HiOrByeCard {

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
    }

}
