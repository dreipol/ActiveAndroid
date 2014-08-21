package ch.dreipol.android.blinq.services.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;



public class Match extends Model {

    @Expose
    @Column
    private Profile mProfile;
    @Expose
    private Boolean mReceived;
    @Expose
    private Integer mMatchId;
    @SerializedName("created_at_datetime")
    @Expose
    private Date mCreatedAt;
    @Expose
    private String mFbId;
    @Expose
    private Boolean mBlocked;

    private Boolean mHasUnread;

    private String mPendingMessage;

    public Match() {
        super();
        mHasUnread = false;
    }

    public Profile getProfile() {
        return mProfile;
    }

    public void setProfile(Profile profile) {
        this.mProfile = profile;
    }

    public Boolean getReceived() {
        return mReceived;
    }

    public Integer getMatchId() {
        return mMatchId;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public String getFbId() {
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

}