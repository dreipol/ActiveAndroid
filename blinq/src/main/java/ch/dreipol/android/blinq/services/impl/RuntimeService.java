package ch.dreipol.android.blinq.services.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;

import ch.dreipol.android.blinq.application.BlinqApplicationFlavour;
import ch.dreipol.android.blinq.services.IRuntimeService;
import ch.dreipol.android.blinq.util.Bog;

/**
 * Created by phil on 17.04.14.
 */
public class RuntimeService extends BaseService implements IRuntimeService {
    @Override
    public LayoutInflater getLayoutInflator(Context context) {
        return null;
    }

    public BlinqApplicationFlavour getFlavour() {
        return BlinqApplicationFlavour.valueOf(getMetadata(BlinqApplicationFlavour.BLINQ_FLAVOUR));
    }

    public String getMetadata(String key) {
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
}