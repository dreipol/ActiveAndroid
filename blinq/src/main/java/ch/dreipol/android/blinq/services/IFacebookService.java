package ch.dreipol.android.blinq.services;

/**
 * Created by phil on 24.03.14.
 */
public interface IFacebookService extends IService{
    boolean hasFacebookSession();

    String getAccessToken();
}
