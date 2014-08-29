package ch.dreipol.android.blinq.services.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.dreipol.android.blinq.application.BlinqApplicationFlavour;
import ch.dreipol.android.blinq.services.IRuntimeService;
import ch.dreipol.android.blinq.util.Bog;

/**
 * Created by phil on 17.04.14.
 */
public class RuntimeService extends BaseService implements IRuntimeService {

    public static final String ANDROID_PLATFORM = "android";

    @Override
    public LayoutInflater getLayoutInflator(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BlinqApplicationFlavour getFlavour() {
        return BlinqApplicationFlavour.valueOf(getMetadataString(BlinqApplicationFlavour.BLINQ_FLAVOUR));
    }

    public String getMetadataString(String key) {
        String result = "unknown value";
        try {
            Context ctx = getService().getContext();
            ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            result = bundle.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            Bog.e(Bog.Category.SYSTEM, "Could not get Metadata", e);
        }
        return result;
    }

    public <T> T getMetadata(String key) {
        Object result = null;
        try {
            Context ctx = getService().getContext();
            ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            result = bundle.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            Bog.e(Bog.Category.SYSTEM, "Could not get Metadata", e);
        }
        return (T) result;
    }

    @Override
    public Map<String, Object> getDeviceInformation() {
        Map<String, Object> map = new HashMap<String, Object>();
        Context context = getService().getContext();
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            map.put("build_number", info.versionCode);
            map.put("app_version", info.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("uuid", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        map.put("system_version", Build.VERSION.RELEASE);
        map.put("type", Build.MODEL);
        map.put("platform", ANDROID_PLATFORM);
        map.put("name", Build.BRAND + "-" + Build.MODEL);
        return map;
    }
}