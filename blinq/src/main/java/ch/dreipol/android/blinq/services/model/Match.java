package ch.dreipol.android.blinq.services.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.naming.AndroidNamingStrategy;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import ch.dreipol.android.dreiworks.activeandroid.Utils;


@Table(name = "matches", columnNaming = AndroidNamingStrategy.class, uniqueIdentifier = "mMatchId")
public class Match extends Model {

    @Column(autoCreate = true, onModelUpdate = Column.ModelUpdateAction.UPDATE )
    private Profile mProfile;

    private boolean mReceived;

    @Column(unique = true, index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long mMatchId;

    @Column
    @SerializedName("created_at_datetime")
    private Date mCreatedAt;

    @Column(index = true)
    private long mFbId;

    private boolean mBlocked;

    @Column
    private boolean mHasUnread;

    @Column
    private String mPendingMessage;

    @Column
    private Date mLastActive;

    public Profile getProfile() {
        return mProfile;
    }

    public void setProfile(Profile profile) {
        this.mProfile = profile;
    }

    public Boolean getReceived() {
        return mReceived;
    }

    public long getMatchId() {
        return mMatchId;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public long getFbId() {
        return mFbId;
    }

    public Boolean isBlocked() {
        return mBlocked;
    }


    public Boolean getHasUnread() {
        return mHasUnread;
    }

    public String getPendingMessage() {
        return mPendingMessage;
    }

    public void setPendingMessage(String pendingMessage) {
        mPendingMessage = pendingMessage;
    }

    public List<ChatMessage> getMessages() {
        try {
            return Utils.getManyAsFrom(ChatMessage.class, this).orderBy("sent").orderBy("sent").execute();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public void setLastActive() {
//        messages
//        NSDate *date = [self.messages valueForKeyPath:@"@max.sent"];
//
//        if (!date)
//            date = self.created_at;

    }

    public int lastSequence() {
        return 0;
    }


    @Override
    public String toString() {
        return "Match{" +
                "mMatchId=" + mMatchId +
                ", name=" + mProfile.getFirstName() +
                '}';
    }

    public void setReceived(boolean received) {
        mReceived = received;
    }
}