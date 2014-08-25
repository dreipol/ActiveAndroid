
package ch.dreipol.android.blinq.services.model;

import com.activeandroid.Model;
import com.activeandroid.naming.AndroidNamingStrategy;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Table(name = "profiles", columnNaming=AndroidNamingStrategy.class)
public class Profile extends Model implements ILoadable{

    @Column(unique = true, index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long mFbId;

    @Column
    private String mDistance;
    @Column
    private String mFirstName;
    @Column
    private String mColorBottom;
    @Column
    private String mLastActive;
    @Column
    private String mColorTop;
    @Column
    private Integer mAge;
    @Column
    private String mSex;

    @Column
    private List<Photo> mPhotos = new ArrayList<Photo>();
    @Column
    private Boolean mLikedMe;
    @Column
    private MutualData mMutualData;
    @Column
    private Boolean mApproved;

    private Map<String, Object> mAdditionalProperties = new HashMap<String, Object>();

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

    public Map<String, Object> getAdditionalProperties() {
        return this.mAdditionalProperties;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "mFirstName='" + mFirstName + '\'' +
                ", mFbId=" + mFbId +
                '}';
    }


}
