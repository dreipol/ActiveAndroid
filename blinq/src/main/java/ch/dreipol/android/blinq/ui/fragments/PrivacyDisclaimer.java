package ch.dreipol.android.blinq.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.widget.LoginButton;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IFacebookService;
import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyDisclaimer extends Fragment {


    private Subscription mSubscribe;

    public PrivacyDisclaimer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View targetView = inflater.inflate(R.layout.fragment_privacy_disclaimer, container, false);

        LoginButton loginButton = (LoginButton) targetView.findViewById(R.id.login_button);
        IFacebookService facebookService = AppService.getInstance().getFacebookService();
        loginButton.setReadPermissions(facebookService.getPermissions());

        targetView.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return targetView;
    }


}
