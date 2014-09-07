package ch.dreipol.android.blinq.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    public int getInt(String key) {
        SharedPreferences preferences = getPreferences();
        return preferences.getInt(key, -1);
    }

    @Override
    public void put(String key, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key, value);
        editor.commit();
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

    @Override
    public Map<String, ?> getEntriesAsMap(final Set<String> keys) {
        SharedPreferences preferences = getPreferences();

        return Maps.filterEntries(preferences.getAll(), new Predicate<Map.Entry<String, ?>>() {
            @Override
            public boolean apply(Map.Entry<String, ?> input) {
                return keys.contains(input.getKey());
            }
        });
    }

    private SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }

    private SharedPreferences getPreferences() {
        return getService().getContext().getSharedPreferences(
                PREFERENCES, Context.MODE_PRIVATE);
    }
}
