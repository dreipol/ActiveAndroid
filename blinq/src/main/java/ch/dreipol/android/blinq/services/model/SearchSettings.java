package ch.dreipol.android.blinq.services.model;

/**
 * Created by phil on 21/08/14.
 */
public class SearchSettings implements ILoadable {

    private Integer mRadius;
    private Integer mFrom;
    private Integer mTo;
    private GenderInterests mInterestedIn;
    private Boolean mVibrate;

    public Integer getRadius() {
        if(mRadius==null){
            mRadius = 0;
        }
        return mRadius;
    }

    public void setRadius(Integer radius) {
        mRadius = radius;
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

    public GenderInterests getInterestedIn() {
        return mInterestedIn;
    }

    public void setInterestedIn(GenderInterests interestedIn) {
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
        result.setRadius(10);
        result.setInterestedIn(GenderInterests.FEMALE);
        result.setVibrate(true);
        return result;
    }
}
