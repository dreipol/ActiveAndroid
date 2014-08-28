package ch.dreipol.android.blinq.services.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.naming.AndroidNamingStrategy;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by melbic on 22/08/14.
 */
@Table(name = "messages", columnNaming = AndroidNamingStrategy.class, id = BaseColumns._ID)
public class ChatMessage extends Model {

    @Column
    private String mMessage;

    @Column
    private int mPk;

    @Column
    private Date mReceivedDate;

    @Column
    private Date mSentDate;

    @Column
    private boolean mHasSendingError;

    @Column
    private UUID mUUID;

    @Column
    private Match mMatch;

    @Column
    private boolean mIsMine;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public int getPk() {
        return mPk;
    }

    public void setHasSendingError(boolean hasSendingError) {
        mHasSendingError = hasSendingError;
    }

    public boolean isHasSendingError() {
        return mHasSendingError;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public static ChatMessage createMessage(Match match) {
        ChatMessage message = new ChatMessage();
        message.mMatch = match;
        message.mIsMine = true;
        message.mUUID = UUID.randomUUID();
        return message;
    }

}
