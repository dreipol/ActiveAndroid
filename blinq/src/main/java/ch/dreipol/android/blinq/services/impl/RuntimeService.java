package ch.dreipol.android.blinq.services.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;

import ch.dreipol.android.blinq.application.BlinqApplicationFlavour;
import ch.dreipol.android.blinq.services.DeviceInformation;
import ch.dreipol.android.blinq.services.IRuntimeService;
import ch.dreipol.android.blinq.util.Bog;
import ch.dreipol.android.blinq.util.StaticResources;

/**
 * Created by phil on 17.04.14.
 */
public class RuntimeService extends BaseService implements IRuntimeService {

    public static final String ANDROID_PLATFORM = "android";

    @Override
    public LayoutInflater getLayoutInflator(Context context) {
        return StaticResources.getLayoutInflator(context);
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
    public DeviceInformation getDeviceInformation() {
        Context context = getService().getContext();
        PackageManager manager = context.getPackageManager();
        DeviceInformation device = new DeviceInformation();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            device.buildNumber = info.versionCode;
            device.appVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        device.uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        device.systemVersion = Build.VERSION.RELEASE;
        device.type = Build.MODEL;
        device.platform = ANDROID_PLATFORM;
        device.name = Build.BRAND + "-" + Build.MODEL;
        return device;
    }

    @Override
    public String getString(int res_id) {
        return getService().getContext().getString(res_id);
    }

}