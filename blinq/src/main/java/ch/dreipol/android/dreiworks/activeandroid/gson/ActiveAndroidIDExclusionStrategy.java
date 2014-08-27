package ch.dreipol.android.dreiworks.activeandroid.gson;

import com.activeandroid.TableInfo;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by melbic on 24/02/14.
 */
public class ActiveAndroidIDExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getName().equals("mId");
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return TableInfo.class.isAssignableFrom(clazz);
    }
}
