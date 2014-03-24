package ch.dreipol.android.blinq.services.impl;

import ch.dreipol.android.blinq.services.ISessionService;

/**
 * Created by phil on 24.03.14.
 */
public class DeviceSessionService extends BaseService implements ISessionService {
    @Override
    public boolean hasFacebookSession() {
        return true;
    }
}
