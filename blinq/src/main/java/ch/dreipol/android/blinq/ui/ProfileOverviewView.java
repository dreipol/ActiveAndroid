package ch.dreipol.android.blinq.ui;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.application.ImageCacheService;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.util.StaticResources;

/**
 * Created by phil on 06/09/14.
 */
public class ProfileOverviewView extends RelativeLayout {

    public ProfileOverviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public ProfileOverviewView(Context context, Profile profile) {
        super(context, null);
        init();
        TextView titleView = (TextView) findViewById(R.id.prename);
        TextView ageView = (TextView) findViewById(R.id.age);
        titleView.setText(profile.getFirstName());
        ageView.setText(profile.getAge().toString());

//        ImageView profileImage = (ImageView) findViewById(R.id.image);

//        AppService.getInstance().getImageCacheService().displayPhoto(profile.getMainPhoto(), profileImage);



    }

    private void init() {
        LayoutInflater inflater = StaticResources.getLayoutInflator(getContext());
        inflater.inflate(R.layout.ui_profile_overview, this, true);
    }
}
