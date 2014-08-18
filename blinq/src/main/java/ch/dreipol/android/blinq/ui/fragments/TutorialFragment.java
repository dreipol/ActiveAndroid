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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        if (mText != null) {
            TextView txtView = (TextView) view.findViewById(R.id.tutorial_text);
            txtView.setText(mText);
        }

        if(mImageId != -9999999){
            ImageView imgView = (ImageView) view.findViewById(R.id.tutorial_image);
            imgView.setImageResource(mImageId);
        }
        return view;
    }


}
