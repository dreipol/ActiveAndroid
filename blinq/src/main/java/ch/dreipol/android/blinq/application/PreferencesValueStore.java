package ch.dreipol.android.blinq.application;

import android.content.Context;
import android.content.SharedPreferences;

import ch.dreipol.android.blinq.services.IValueStoreService;
import ch.dreipol.android.blinq.services.impl.BaseService;

/**
 * Created by phil on 25.03.14.
 */
public class PreferencesValueStore extends BaseService implements IValueStoreService {

    public static final String PREFERENCES = "BLINQ_PREFERENCES";

    @Override
    public String get(String key) {
        SharedPreferences preferences = getPreferences();
        return preferences.getString(key, null);
    }


    @Override
    public void put(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.commit();

    }

    @Override
    public boolean has(String key) {
        return getPreferences().contains(key) && get(key).length() > 0;
    }

    @Override
    public void clear(String key) {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(key);
        editor.commit();
    }

    private SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }

    private SharedPreferences getPreferences() {
        return getService().getContext().getSharedPreferences(
                PREFERENCES, Context.MODE_PRIVATE);
    }
}
