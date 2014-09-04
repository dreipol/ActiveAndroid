package ch.dreipol.android.blinq.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import ch.dreipol.android.blinq.services.model.ChatMessage;
import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.ui.lists.ChatListItem;
import ch.dreipol.android.blinq.ui.lists.MatchListItemView;

/**
 * Created by phil on 04/09/14.
 */
public class ChatCursorAdapter extends CursorAdapter {
    public ChatCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return new ChatListItem(context, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ChatListItem chatItemView = (ChatListItem)view;

        ChatMessage msg = new ChatMessage();
        msg.loadFromCursor(cursor);
        chatItemView.setMessage(msg);
    }
}
