package ch.dreipol.android.blinq.ui.fragments.facebook;


import android.app.Activity;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.Collection;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.facebook.FacebookPhoto;
import ch.dreipol.android.blinq.services.model.facebook.FacebookPhotoSource;
import ch.dreipol.android.blinq.ui.fragments.BlinqFragment;
import ch.dreipol.android.blinq.ui.fragments.LoadingState;
import ch.dreipol.android.blinq.ui.lists.FacebookPhotoListItemView;
import rx.Subscription;
import rx.functions.Action1;

public class FacebookPhotosFragment extends BlinqFragment {


    public static final String ALBUM_ID = "ALBUM_ID";
    protected Subscription mSubscription;
    private OnPhotoInteractionListener mListener;

    public FacebookPhotosFragment() {
    }


    protected void loadData() {
        mSubscription = AppService.getInstance().getFacebookService().getPhotosFromAlbum(getAlbumId()).subscribe(new Action1<FacebookService.FacebookAlbumResponse>() {
            @Override
            public void call(FacebookService.FacebookAlbumResponse facebookAlbumResponse) {
                LoadingInfo<FacebookService.FacebookAlbumResponse> loadingInfo = new LoadingInfo<FacebookService.FacebookAlbumResponse>(LoadingState.LOADED);
                loadingInfo.setData(facebookAlbumResponse);
                mDataSubject.onNext(loadingInfo);
            }
        });
    }

    private String getAlbumId() {
        return getArguments().getString(ALBUM_ID);
    }

    @Override
    public void onStart() {
        super.onStart();

        loadData();

        mLoadingSubscription.subscribe(new Action1<LoadingInfo>() {
            @Override
            public void call(LoadingInfo loadingInfo) {
                final FacebookService.FacebookAlbumResponse albumResponse = (FacebookService.FacebookAlbumResponse) loadingInfo.getData();
                Collection<FacebookPhoto> data = albumResponse.mData;
                final FacebookPhoto[] facebookPhotos = data.toArray(new FacebookPhoto[data.size()]);




                View container = loadingInfo.getViewContainer();
                GridView gridView = (GridView) container.findViewById(R.id.facebook_photos_grid);


                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(mListener!=null){

                            mListener.didSelectPhoto(facebookPhotos[position]);
                        }
                    }
                });
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
                        FacebookPhoto facebookPhoto = facebookPhotos[position];
                        result.setImage(facebookPhoto.getPicture());
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPhotoInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPhotoInteractionListener");
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_facebook_photos;
    }

    public interface OnPhotoInteractionListener {
        void didSelectPhoto(FacebookPhoto photo);
    }
}
