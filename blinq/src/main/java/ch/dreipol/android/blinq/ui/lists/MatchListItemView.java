package ch.dreipol.android.blinq.ui.lists;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.RoundedImageView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.services.model.Profile;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by phil on 27/08/14.
 */
public class MatchListItemView extends RelativeLayout {
    private final RoundedImageView mProfileImage;
    private final TextView mStatusTxt;
    private final TextView mMatchTitle;
    private Subscription mPhotoSubscription;
//    private BehaviorSubject<LoadingInfo> mPhotoSubscription;

    public MatchListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = AppService.getInstance().getRuntimeService().getLayoutInflator(context);
        inflater.inflate(R.layout.ui_matches_list_item, this, true);

        mProfileImage = (RoundedImageView) findViewById(R.id.profile_picture);
        mMatchTitle = (TextView) findViewById(R.id.match_name);
        mStatusTxt = (TextView) findViewById(R.id.status_text);
    }



    public void setMatch(Match match) {
        Profile profile = match.getProfile();
        mMatchTitle.setText(profile.getFirstName());

        if (mPhotoSubscription != null) {
            mPhotoSubscription.unsubscribe();

        }

        mPhotoSubscription = AppService.getInstance().getImageCacheService().displayPhoto(profile.getMainPhoto(), mProfileImage).subscribe(new Action1<LoadingInfo>() {
            @Override
            public void call(LoadingInfo loadingInfo) {
//

            }
        });


    }

}

