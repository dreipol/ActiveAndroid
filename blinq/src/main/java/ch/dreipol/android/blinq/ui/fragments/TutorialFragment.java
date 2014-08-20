package ch.dreipol.android.blinq.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialFragment extends Fragment {


    public static final String IMAGE_ID = "IMG";
    public static final String TEXT = "TXT";
    private String mText;
    private int mImageId;
    private boolean mShowMasquerade;

    public TutorialFragment() {
        // Required empty public constructor
        mImageId = -9999999;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
//            mImageString = savedInstanceState.getString(IMAGE_ID);
            mText = args.getString(TEXT);
            mImageId = args.getInt(IMAGE_ID);
            mShowMasquerade = args.getBoolean(JoinBlinqFragment.SHOW_MASQUERADE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        TextView txtView = (TextView) view.findViewById(R.id.tutorial_text);
        if (mText != null && !mShowMasquerade) {
            txtView.setText(mText);
            txtView.setVisibility(View.VISIBLE);
        }else{
           txtView.setVisibility(View.INVISIBLE);
        }

        if(mImageId != -9999999){
            ImageView imgView = (ImageView) view.findViewById(R.id.tutorial_image);
            imgView.setImageResource(mImageId);
        }

        return view;
    }


}
