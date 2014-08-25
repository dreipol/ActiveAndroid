package ch.dreipol.android.blinq.ui.fragments.facebook;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.ui.fragments.LoadingState;
import rx.functions.Action1;

/**
 * Created by phil on 25/08/14.
 */
public class FacebookPhotosOfMeFragment extends FacebookPhotosFragment {
    @Override
    protected void loadData() {
        mSubscription = AppService.getInstance().getFacebookService().getPhotosOfMe().subscribe(new Action1<FacebookService.FacebookAlbumResponse>() {
            @Override
            public void call(FacebookService.FacebookAlbumResponse facebookAlbumResponse) {
                LoadingInfo<FacebookService.FacebookAlbumResponse> loadingInfo = new LoadingInfo<FacebookService.FacebookAlbumResponse>(LoadingState.LOADED);
                loadingInfo.setData(facebookAlbumResponse);
                mDataSubject.onNext(loadingInfo);
            }
        });
    }
}
