
package ch.dreipol.android.blinq.services.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {

    private String mDistance;
    private String mFirstName;
    private String mColorBottom;
    private String mLastActive;
    private String mColorTop;
    private Integer mAge;
    private long mFbId;
    private String mSex;
    private List<Photo> mPhotos = new ArrayList<Photo>();
    private Boolean mLikedMe;
    private MutualData mMutualData;
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
