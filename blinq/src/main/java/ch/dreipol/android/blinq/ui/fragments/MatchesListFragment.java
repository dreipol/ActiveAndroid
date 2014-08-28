package ch.dreipol.android.blinq.ui.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.model.ILoadable;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.ui.adapters.MatchListCursorAdapter;
import ch.dreipol.android.blinq.util.Bog;
import rx.functions.Action1;

/**
 * Created by phil on 21.03.14.
 */
public class MatchesListFragment extends BlinqFragment {

    private ListView mListView;
    private MatchListCursorAdapter mAdapter;
    private Loader<Cursor> mCursorLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();


        mLoadingSubscription.subscribe(new Action1<LoadingInfo>() {
            @Override
            public void call(LoadingInfo loadingInfo) {

                View viewContainer = loadingInfo.getViewContainer();
                mListView = (ListView) viewContainer.findViewById(R.id.matches_list);


                Cursor cursor = ActiveAndroid.getDatabase().rawQuery(new Select().from(Match.class).toSql(), null);


                FragmentActivity activity = MatchesListFragment.this.getActivity();

                mAdapter = new MatchListCursorAdapter(activity, cursor, 0);


                mCursorLoader = getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int arg0, Bundle cursor) {
                        Uri uri = ContentProvider.createUri(Match.class, null);
                        return new CursorLoader(getActivity(),
                                uri,
                                null, null, null, null
                        );
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
                        mAdapter.swapCursor(cursor);
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> arg0) {
                        mAdapter.swapCursor(null);
                    }
                });


                mListView.setAdapter(mAdapter);
                mCursorLoader.startLoading();
            }

        });

        mDataSubject.onNext(new LoadingInfo(LoadingState.LOADED));

//        AppService.getInstance().getNetworkService().loadMatches().subscribe(new Action1<ArrayList<Match>>() {
//            @Override
//            public void call(ArrayList<Match> matches) {
//                    LoadingInfo v = new LoadingInfo(LoadingState.LOADED);
//                v.setData(new MatchData(matches));
//                mDataSubject.onNext(v);
//
//            }
//        });

//
//        mDataSubject.onNext(new LoadingInfo(LoadingState.LOADED));
//
//
//        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
//            @Override
//            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//                return new CursorLoader(getActivity(),
//                        ContentProvider.createUri(Match.class, null),
//                        null, null, null, null
//                );
//            }
//
//            @Override
//            public void onLoadFinished(Loader<Cursor> objectLoader, Cursor o) {
//
//            }
//
//            @Override
//            public void onLoaderReset(Loader<Cursor> objectLoader) {
//
//            }
//        });
//
//

//
//        mLoadingSubscription.subscribe(new Action1<LoadingInfo>() {
//            @Override
//            public void call(LoadingInfo loadingInfo) {
//
//                View viewContainer = loadingInfo.getViewContainer();
//
//
//                MatchData data = (MatchData) loadingInfo.getData();
//                final ArrayList<Match> matches = data.getMatches();
//
//                mMatchesList = (ListView) viewContainer.findViewById(R.id.matches_list);
//
//
//
//                mMatchesList.setAdapter(new ListAdapter() {
//                    @Override
//                    public boolean areAllItemsEnabled() {
//                        return true;
//                    }
//
//                    @Override
//                    public boolean isEnabled(int position) {
//                        return true;
//                    }
//
//                    @Override
//                    public void registerDataSetObserver(DataSetObserver observer) {
//
//                    }
//
//                    @Override
//                    public void unregisterDataSetObserver(DataSetObserver observer) {
//
//                    }
//
//                    @Override
//                    public int getCount() {
//                        return matches.size();
//                    }
//
//                    @Override
//                    public Object getItem(int position) {
//                        return matches.get(position);
//                    }
//
//                    @Override
//                    public long getItemId(int position) {
//                        return matches.get(position).hashCode();
//                    }
//
//                    @Override
//                    public boolean hasStableIds() {
//                        return true;
//                    }
//
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        MatchListItemView result;
//                        if(convertView == null){
//                            result = new MatchListItemView(parent.getContext(), null);
//                        }else{
//                            result = (MatchListItemView) convertView;
//                        }
//                        result.setMatch(matches.get(position));
//                        return result;
//                    }
//
//                    @Override
//                    public int getItemViewType(int position) {
//                        return 0;
//                    }
//
//                    @Override
//                    public int getViewTypeCount() {
//                        return 1;
//                    }
//
//                    @Override
//                    public boolean isEmpty() {
//                        return false;
//                    }
//                });
//            }
//        });
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_matches_list;
    }

    class MatchData implements ILoadable {
        private ArrayList<Match> mMatches;

        public MatchData(ArrayList<Match> matches) {

            mMatches = matches;
        }

        public ArrayList<Match> getMatches() {
            return mMatches;
        }
    }
}
