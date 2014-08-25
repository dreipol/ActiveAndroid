package ch.dreipol.android.blinq.ui.fragments.facebook;


import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.Collection;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.facebook.FacebookPhoto;
import ch.dreipol.android.blinq.ui.fragments.BlinqFragment;
import ch.dreipol.android.blinq.ui.fragments.LoadingState;
import ch.dreipol.android.blinq.ui.lists.FacebookPhotoListItemView;
import rx.Subscription;
import rx.functions.Action1;

public class FacebookPhotosFragment extends BlinqFragment {


    public static final String ALBUM_ID = "ALBUM_ID";
    public static final String ALBUM_ME = "PHOTOS_OF_ME";
    private Subscription mSubscription;

    public FacebookPhotosFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();
        final String albumId = getArguments().getString(ALBUM_ID);
        if (albumId.equals(ALBUM_ME)) {
            mSubscription = AppService.getInstance().getFacebookService().getPhotosOfMe().subscribe(new Action1<FacebookService.FacebookAlbumResponse>() {
                @Override
                public void call(FacebookService.FacebookAlbumResponse facebookAlbumResponse) {
                    LoadingInfo<FacebookService.FacebookAlbumResponse> loadingInfo = new LoadingInfo<FacebookService.FacebookAlbumResponse>(LoadingState.LOADED);
                    loadingInfo.setData(facebookAlbumResponse);
                    mDataSubject.onNext(loadingInfo);
                }
            });

        } else {
            mSubscription = AppService.getInstance().getFacebookService().getPhotosFromAlbum(albumId).subscribe(new Action1<FacebookService.FacebookAlbumResponse>() {
                @Override
                public void call(FacebookService.FacebookAlbumResponse facebookAlbumResponse) {
                    LoadingInfo<FacebookService.FacebookAlbumResponse> loadingInfo = new LoadingInfo<FacebookService.FacebookAlbumResponse>(LoadingState.LOADED);
                    loadingInfo.setData(facebookAlbumResponse);
                    mDataSubject.onNext(loadingInfo);
                }
            });
        }


        mLoadingSubscription.subscribe(new Action1<LoadingInfo>() {
            @Override
            public void call(LoadingInfo loadingInfo) {
                final FacebookService.FacebookAlbumResponse albumResponse = (FacebookService.FacebookAlbumResponse) loadingInfo.getData();
                Collection<FacebookPhoto> data = albumResponse.mData;
                final FacebookPhoto[] facebookPhotos = data.toArray(new FacebookPhoto[data.size()]);


                View container = loadingInfo.getViewContainer();
                GridView gridView = (GridView) container.findViewById(R.id.facebook_photos_grid);
                gridView.setAdapter(new ListAdapter() {
                    @Override
                    public boolean areAllItemsEnabled() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled(int position) {
                        return true;
                    }

                    @Override
                    public void registerDataSetObserver(DataSetObserver observer) {

                    }

                    @Override
                    public void unregisterDataSetObserver(DataSetObserver observer) {

                    }

                    @Override
                    public int getCount() {
                        return facebookPhotos.length;
                    }

                    @Override
                    public Object getItem(int position) {
                        return facebookPhotos[position];
                    }

                    @Override
                    public long getItemId(int position) {
                        return facebookPhotos[position].hashCode();
                    }

                    @Override
                    public boolean hasStableIds() {
                        return true;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        FacebookPhotoListItemView result = null;
                        if (convertView == null) {
                            result = new FacebookPhotoListItemView(parent.getContext());

                        } else {
                            result = (FacebookPhotoListItemView) convertView;
                        }
                        result.setImage(facebookPhotos[position].getPicture());
                        return result;
                    }

                    @Override
                    public int getItemViewType(int position) {
                        return 0;
                    }

                    @Override
                    public int getViewTypeCount() {
                        return 1;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }
                });

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubscription.unsubscribe();
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_facebook_photos;
    }


}
