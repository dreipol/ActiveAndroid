package ch.dreipol.android.blinq.services.network;

import android.location.Location;

import ch.dreipol.android.blinq.services.model.GenderInterests;
import ch.dreipol.android.blinq.services.model.SettingsProfile;

/**
 * Created by melbic on 01/09/14.
 */
public class UploadProfile {

    private String mLanguage;
    private String mColorBottom;
    private String mColorTop;
    private boolean mInstaconnectIsEnabled = false;
    private GenderInterests mInterestedIn;
    private boolean mLoginCompleted;
    private String mSex;
    private Location mLocation;

    public UploadProfile(SettingsProfile object) {
        super();
        setProfile(object);
    }

    private void setProfile(SettingsProfile profile) {
        mColorBottom = profile.getColorBottom();
        mColorTop = profile.getColorTop();
        mInterestedIn = profile.getInterestedIn();
        mLoginCompleted = profile.isLoginCompleted();
        mSex = profile.getSex();
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }
}
