package ch.dreipol.android.blinq.ui.fragments;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import ch.dreipol.android.blinq.R;

import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.services.model.facebook.FacebookAlbum;
import ch.dreipol.android.blinq.ui.lists.FacebookAlbumListItemView;
import rx.functions.Action1;

public class FacebookAlbumFragment extends ListFragment {

    private OnAlbumInteractionListener mListener;

    public FacebookAlbumFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppService.getInstance().getFacebookService().getAlbums().subscribe(new Action1<FacebookService.FacebookAlbumListResponse>() {
            @Override
            public void call(final FacebookService.FacebookAlbumListResponse facebookAlbumListResponse) {
                final FacebookAlbum[] albums = facebookAlbumListResponse.mData.toArray(new FacebookAlbum[facebookAlbumListResponse.mData.size()]);

                getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (null != mListener) {

                            if (position > 0) {
                                FacebookAlbum album = albums[position - 1];
                                mListener.didSelectAlbum(album);
                            } else {
                                mListener.didSelectPhotosOfMe();
                            }
                        }


                    }
                });

                setListAdapter(new ListAdapter() {
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
                        return albums.length + 1;
                    }

                    @Override
                    public Object getItem(int position) {
                        return albums[position - 1];
                    }

                    @Override
                    public long getItemId(int position) {
                        if (position == 0) {
                            return 0;
                        }
                        return albums[position - 1].getId().hashCode();
                    }

                    @Override
                    public boolean hasStableIds() {
                        return true;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        FacebookAlbumListItemView txtView;
                        if (convertView != null) {
                            txtView = (FacebookAlbumListItemView) convertView;
                        } else {
                            txtView = new FacebookAlbumListItemView(parent.getContext());
                        }
                        if (position > 0) {
                            FacebookAlbum album = albums[position - 1];

                            txtView.setText(album.getName());
                            txtView.setImage(album.getCoverId());

                        } else {
                            txtView.setText(getResources().getString(R.string.photos_of_you));
                            txtView.setImage(null);
                        }


                        return txtView;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAlbumInteractionListener) activity;
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


    public interface OnAlbumInteractionListener {
        void didSelectAlbum(FacebookAlbum album);

        void didSelectPhotosOfMe();
    }

}
