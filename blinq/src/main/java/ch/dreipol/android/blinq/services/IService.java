package ch.dreipol.android.blinq.services;

/**
 * Created by phil on 22.03.14.
 */
public interface IService {
    void dispose();

    void setup(AppService appService);

    AppService getService();

}
