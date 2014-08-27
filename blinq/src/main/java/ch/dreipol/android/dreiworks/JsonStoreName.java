package ch.dreipol.android.dreiworks;

/**
 * Created by melbic on 27/08/14.
 */
public enum JsonStoreName {
    SETTINGS_PROFILE("SettingsProfile");
    private final String mName;

    private JsonStoreName(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }
}
