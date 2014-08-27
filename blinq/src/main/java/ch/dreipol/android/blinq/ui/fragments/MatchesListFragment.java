package ch.dreipol.android.blinq.ui.fragments;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.model.ILoadable;
import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.ui.lists.MatchListItemView;
import rx.functions.Action1;

/**
 * Created by phil on 21.03.14.
 */
public class MatchesListFragment extends BlinqFragment {

    private ListView mMatchesList;

    @Override
    public void onStart() {

//        AppService.getInstance().getNetworkService().loadMatches().subscribe(new Action1<ArrayList<Match>>() {
//            @Override
//            public void call(ArrayList<Match> matches) {
//                    LoadingInfo v = new LoadingInfo(LoadingState.LOADED);
//                v.setData(new MatchData(matches));
//                mDataSubject.onNext(v);
//
//            }
//        });


        mDataSubject.onNext(new LoadingInfo(LoadingState.LOADED));






        mLoadingSubscription.subscribe(new Action1<LoadingInfo>() {
            @Override
            public void call(LoadingInfo loadingInfo) {

                View viewContainer = loadingInfo.getViewContainer();


                MatchData data = (MatchData) loadingInfo.getData();
                final ArrayList<Match> matches = data.getMatches();

                mMatchesList = (ListView) viewContainer.findViewById(R.id.matches_list);

                

                mMatchesList.setAdapter(new ListAdapter() {
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
                        return matches.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return matches.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return matches.get(position).hashCode();
                    }

                    @Override
                    public boolean hasStableIds() {
                        return true;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        MatchListItemView result;
                        if(convertView == null){
                            result = new MatchListItemView(parent.getContext(), null);
                        }else{
                            result = (MatchListItemView) convertView;
                        }
                        result.setMatch(matches.get(position));
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
