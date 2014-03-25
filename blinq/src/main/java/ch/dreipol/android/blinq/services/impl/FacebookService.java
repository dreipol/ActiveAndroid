package ch.dreipol.android.blinq.services.impl;

import com.facebook.Session;

import ch.dreipol.android.blinq.services.IFacebookService;

/**
 * Created by phil on 24.03.14.
 */
public class FacebookService extends BaseService implements IFacebookService {


    @Override
    public boolean hasFacebookSession() {
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getAccessToken(){
        return Session.getActiveSession().getAccessToken();
    }

}