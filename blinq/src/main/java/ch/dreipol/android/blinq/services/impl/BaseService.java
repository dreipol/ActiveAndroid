package ch.dreipol.android.blinq.services.impl;


import ch.dreipol.android.blinq.services.AppService;
import ch.dreipol.android.blinq.services.IService;

/**
 * Created by phil on 22.03.14.
 */
public abstract class BaseService  implements IService {


    private AppService mAppService;


    @Override
    public void setup(AppService appService) {
        mAppService = appService;
    }

    public AppService getService() {
        return mAppService;
    }

    @Override
    public void dispose() {
        mAppService = null;
    }
}
