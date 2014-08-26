package ch.dreipol.android.blinq.services.model;

import java.util.Date;
import java.util.EnumSet;

/**
 * Created by melbic on 25/08/14.
 */
public class SettingsProfile extends Profile {
    public SettingsProfile() {
        super();
    }

    private int mFriendCount;
    private boolean mLoginCompleted;
    private GenderInterests mInterestedIn;
    private float mHotness;
    private Date mApprovalDate;
    private Date mBirthday;

    public int getFriendCount() {
        return mFriendCount;
    }

    public boolean isLoginCompleted() {
        return mLoginCompleted;
    }

    public float getHotness() {
        return mHotness;
    }

    public GenderInterests getInterestedIn() {
        return mInterestedIn;
    }

    public Date getBirthday() {
        return mBirthday;
    }

    public Date getApprovalDate() {
        return mApprovalDate;
    }
}
