
package ch.dreipol.android.blinq.services.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import rx.functions.Action1;

public class Photo implements Serializable {
    private long mPk;
    private String mThumbId;
    private String mProfileId;
    private String mFullId;
    private long mObjectId;
    private int mEviction;
    @Expose
    private Date mExpireDate;

    public long getPk() {
        return mPk;
    }

    public String getFullId() {
        return mFullId;
    }

    public String getProfileId() {
        return mProfileId;
    }

    public long getObjectId() {
        return mObjectId;
    }

    public String getThumbId() {
        return mThumbId;
    }

    public int getEviction() {
        return mEviction;
    }

    public boolean isExpired() {
//        return true;
        return mExpireDate.before(new Date());
    }

    public void setExpireDate() {
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.MINUTE, mEviction);
        mExpireDate = calendar.getTime();
    }

    private void updateWithOther(Photo other) {
        if (other.mPk != this.mPk) {
            throw new RuntimeException("The updating photo hasn't got the same pk as the old one.");
        }
        mObjectId = other.mObjectId;
        mThumbId = other.mThumbId;
        mProfileId = other.mProfileId;
        mFullId = other.mFullId;
        mEviction = other.mEviction;
    }

    public Action1<Photo> getUpdateAction() {
        return new PhotoUpdateAction();
    }

    class PhotoUpdateAction implements Action1<Photo> {
        @Override
        public void call(Photo photo) {
            Photo.this.updateWithOther(photo);
        }
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mPk=" + mPk +
                ", mObjectId=" + mObjectId +
                ", mExpireDate=" + mExpireDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        if (mObjectId != photo.mObjectId) return false;
        if (mPk != photo.mPk) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (mPk ^ (mPk >>> 32));
        result = 31 * result + (int) (mObjectId ^ (mObjectId >>> 32));
        return result;
    }
}
