package ch.dreipol.android.blinq.services.model;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

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

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public void removePhoto(Photo photo) {
        mPhotos.remove(photo);
    }
}
