package ch.dreipol.android.blinq.ui.lists;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ch.dreipol.android.blinq.services.model.ChatMessage;

/**
 * Created by phil on 04/09/14.
 */
public class ChatListItem extends TextView{
    public ChatListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setMessage(ChatMessage msg) {
        setText(msg.getMessage());
    }
}
