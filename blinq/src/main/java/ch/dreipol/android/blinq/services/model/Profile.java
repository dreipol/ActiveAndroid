
package ch.dreipol.android.blinq.services.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.naming.AndroidNamingStrategy;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "profiles", columnNaming = AndroidNamingStrategy.class, uniqueIdentifier = "mFbId", id = BaseColumns._ID)
public class Profile extends Model implements ILoadable {

    @Column(unique = true, index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long mFbId;

    @Column(onModelUpdate = Column.ModelUpdateAction.REPLACE)
    private String mDistance;
    @Column(onModelUpdate = Column.ModelUpdateAction.REPLACE)
    private String mFirstName;
    @Column(onModelUpdate = Column.ModelUpdateAction.REPLACE)
    private String mColorBottom;
    @Column(onModelUpdate = Column.ModelUpdateAction.REPLACE)
    private String mLastActive;
    @Column(onModelUpdate = Column.ModelUpdateAction.REPLACE)
    private String mColorTop;
    @Column(onModelUpdate = Column.ModelUpdateAction.REPLACE)
    private Integer mAge;
    @Column(onModelUpdate = Column.ModelUpdateAction.REPLACE)
    private String mSex;

    @Column(onModelUpdate = Column.ModelUpdateAction.REPLACE)
    protected List<Photo> mPhotos = new ArrayList<Photo>();
    private Boolean mLikedMe;
    @Column(onModelUpdate = Column.ModelUpdateAction.REPLACE)
    private MutualData mMutualData;
    private Boolean mApproved;

    public String getDistance() {
        return mDistance;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getColorBottom() {
        return mColorBottom;
    }

    public String getLastActive() {
        return mLastActive;
    }

    public String getColorTop() {
        return mColorTop;
    }

    public Integer getAge() {
        return mAge;
    }

    public Long getFbId() {
        return mFbId;
    }

    public String getSex() {
        return mSex;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public Boolean getLikedMe() {
        return mLikedMe;
    }


    public MutualData getMutualData() {
        return mMutualData;
    }

    public void setMutualData(MutualData mutualData) {
        this.mMutualData = mutualData;
    }

    public Boolean getApproved() {
        return mApproved;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "mFirstName='" + mFirstName + '\'' +
                ", mFbId=" + mFbId +
                '}';
    }


    public Photo getMainPhoto() {
        Photo result = null;
        List<Photo> photos = getPhotos();
        if (photos.size() > 0) {
            result = photos.get(0);
        }
        return result;
    }

}
