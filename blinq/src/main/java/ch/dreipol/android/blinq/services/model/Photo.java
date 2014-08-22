
package ch.dreipol.android.blinq.services.model;

import java.io.Serializable;

public class Photo implements Serializable {
    private Long mPk;
    private String mFullId;
    private String mProfileId;
    private Long mObjectId;
    private String mThumbId;
    private Integer mEviction;

    public Long getPk() {
        return mPk;
    }

    public String getFullId() {
        return mFullId;
    }

    public String getProfileId() {
        return mProfileId;
    }

    public Long getObjectId() {
        return mObjectId;
    }

    public String getThumbId() {
        return mThumbId;
    }

    public Integer getEviction() {
        return mEviction;
    }


}
