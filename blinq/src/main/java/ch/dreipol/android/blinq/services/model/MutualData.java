
package ch.dreipol.android.blinq.services.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutualData implements Serializable{
    private List<Long> mSchools = new ArrayList<Long>();
    private List<Long> mLikes = new ArrayList<Long>();
    private List<Long> mPlaces = new ArrayList<Long>();

    public List<Long> getSchools() {
        return mSchools;
    }

    public List<Long> getLikes() {
        return mLikes;
    }

    public List<Long> getPlaces() {
        return mPlaces;
    }

    public int count() {
        return mSchools.size() + mLikes.size() + mPlaces.size();
    }
}
