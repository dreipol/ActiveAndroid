package ch.dreipol.android.blinq.services.model;

/**
 * Created by phil on 21/08/14.
 */
public class SearchSettings implements ILoadable{

    public enum SearchInterest {
        MALE, FEMALE, BOTH
    }
    private Integer mDistance;
    private Integer mFrom;
    private Integer mTo;
    private SearchInterest mInterestedIn;
    private Boolean mVibrate;

    public Integer getDistance() {
        return mDistance;
    }

    public void setDistance(Integer distance) {
        mDistance = distance;
    }

    public Integer getFrom() {
        return mFrom;
    }

    public void setFrom(Integer from) {
        mFrom = from;
    }

    public Integer getTo() {
        return mTo;
    }

    public void setTo(Integer to) {
        mTo = to;
    }

    public SearchInterest getInterestedIn() {
        return mInterestedIn;
    }

    public void setInterestedIn(SearchInterest interestedIn) {
        mInterestedIn = interestedIn;
    }

    public Boolean getVibrate() {
        return mVibrate;
    }

    public void setVibrate(Boolean vibrate) {
        mVibrate = vibrate;
    }


    public static SearchSettings defaultSettings() {
        SearchSettings result = new SearchSettings();
        result.setFrom(18);
        result.setTo(20);
        result.setDistance(10);
        result.setInterestedIn(SearchInterest.FEMALE);
        result.setVibrate(true);
        return result;
    }
}
