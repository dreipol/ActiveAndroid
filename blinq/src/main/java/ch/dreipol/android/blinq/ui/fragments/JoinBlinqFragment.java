package ch.dreipol.android.blinq.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.ui.PageIndicatorView;

public class JoinBlinqFragment extends Fragment {

    public static final String SHOW_MASQUERADE = "MASQUERADE";
    private JoinBlinqFragmentListener mListener;
    private ViewPager mViewPager;
    private TutorialPagerAdapter mPagerAdaper;
    private PageIndicatorView mpageIndicator;
    private boolean mShowMasquerade;

    public static JoinBlinqFragment newInstance() {
        JoinBlinqFragment fragment = new JoinBlinqFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public JoinBlinqFragment() {
        mShowMasquerade = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mShowMasquerade = arguments.getBoolean(SHOW_MASQUERADE, false);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View targetView = inflater.inflate(R.layout.fragment_join_blinq_screen, container, false);
        mPagerAdaper = new TutorialPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) targetView.findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdaper);

        if (mShowMasquerade) {
            targetView.findViewById(R.id.join_button).setVisibility(View.GONE);
        }
        mpageIndicator = (PageIndicatorView) targetView.findViewById(R.id.indicator);
        mpageIndicator.setNumberOfPages(mPagerAdaper.getCount());
//        mPageIndicatorView = (PageIndicatorView) targetView.findViewById(R.id.pager);
        return targetView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (JoinBlinqFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface JoinBlinqFragmentListener {
        public void showLoginScreen();

        public void showNextScreen();
    }

    public class TutorialPagerAdapter extends FragmentPagerAdapter {
        public TutorialPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            TutorialFragment fragment = new TutorialFragment();
            Bundle args = new Bundle();


            String name = "tutorialText" + (i + 1);
            String imageName = "tutorialImage" + (i + 1);
            String packageName = getActivity().getPackageName();
            int textIdentifier = getResources().getIdentifier(name, "string", packageName);

            args.putString(TutorialFragment.TEXT, getResources().getString(textIdentifier));
            int translatedImageIdentifier = getResources().getIdentifier(imageName, "string", packageName);
            String string = getResources().getString(translatedImageIdentifier);
            int imageIdentifier = getResources().getIdentifier(string, "drawable", packageName);
            args.putInt(TutorialFragment.IMAGE_ID, imageIdentifier);
            args.putBoolean(SHOW_MASQUERADE, mShowMasquerade);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "TUTORIAL " + (position + 1);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            mpageIndicator.setActivePage(mViewPager.getCurrentItem());


        }
    }


}
