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

public class JoinBlinqFragment extends Fragment {

    private JoinBlinqFragmentListener mListener;
    private ViewPager mViewPager;
    private TutorialPagerAdapter mPagerAdaper;

    public static JoinBlinqFragment newInstance() {
        JoinBlinqFragment fragment = new JoinBlinqFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public JoinBlinqFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View targetView = inflater.inflate(R.layout.fragment_join_blinq_screen, container, false);
        mPagerAdaper = new TutorialPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) targetView.findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdaper);
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
    }

    public class TutorialPagerAdapter extends FragmentPagerAdapter{
        public TutorialPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            TutorialFragment fragment = new TutorialFragment();
            Bundle args = new Bundle();
            // Our object is just an integer :-P
//            args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
            args.putString(TutorialFragment.TEXT, "Hello World");
            args.putString(TutorialFragment.IMAGE, "Hello World");
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
    }


}
