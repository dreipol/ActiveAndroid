package ch.dreipol.android.blinq.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import java.util.List;

import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.services.model.Profile;
import ch.dreipol.android.blinq.ui.lists.MatchListItemView;
import ch.dreipol.android.blinq.util.Bog;
import rx.functions.Action1;

/**
 * Created by phil on 27/08/14.
 */
public class MatchListCursorAdapter extends CursorAdapter {
    public MatchListCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return new MatchListItemView(context, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MatchListItemView matchItemView = (MatchListItemView)view;

        Match match = new Match();
        match.loadFromCursor(cursor);
        matchItemView.setMatch(match);
    }


}
