package ch.dreipol.android.blinq.services.impl;

import ch.dreipol.android.blinq.services.AppService;

/**
 * Created by melbic on 21/08/14.
 */
public class DatabaseDebugService extends DatabaseService {
    @Override
    public void setup(AppService appService) {
        super.setup(appService);
        Boolean reset_database = getService().getRuntimeService().<Boolean>getMetadata("RESET_DATABASE");
        if (reset_database) {
            resetDatabase();
        }
    }
}
