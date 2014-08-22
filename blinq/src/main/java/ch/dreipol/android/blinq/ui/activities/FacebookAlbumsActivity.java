package ch.dreipol.android.blinq.ui.activities;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.impl.FacebookService;
import ch.dreipol.android.blinq.services.model.facebook.FacebookAlbum;
import rx.functions.Action1;

public class FacebookAlbumsActivity extends Activity {

    private ListView mAlbumsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_albums);
        mAlbumsList = (ListView) findViewById(R.id.facebook_albums_list);

        AppService.getInstance().getFacebookService().getAlbums().subscribe(new Action1<FacebookService.FacebookAlbumListResponse>() {
            @Override
            public void call(final FacebookService.FacebookAlbumListResponse facebookAlbumListResponse) {
                final FacebookAlbum[] albums = facebookAlbumListResponse.mData.toArray(new FacebookAlbum[facebookAlbumListResponse.mData.size()]);


                mAlbumsList.setAdapter(new ListAdapter() {
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
                        return albums.length;
                    }

                    @Override
                    public Object getItem(int position) {
                        return albums[position];
                    }

                    @Override
                    public long getItemId(int position) {
                        return albums[position].getId().hashCode();
                    }

                    @Override
                    public boolean hasStableIds() {
                        return true;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView txtView;
                        if(convertView!=null){
                            txtView = (TextView) convertView;
                        }else{
                            txtView = new TextView(parent.getContext());
                        }

                        txtView.setText(albums[position].getName());
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


}
