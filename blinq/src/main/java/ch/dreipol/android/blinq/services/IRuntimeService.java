package ch.dreipol.android.blinq.services;

import android.content.Context;
import android.view.LayoutInflater;

import ch.dreipol.android.blinq.application.BlinqApplicationFlavour;

/**
 * Created by phil on 17.04.14.
 */
public interface IRuntimeService  extends IService{
    public LayoutInflater getLayoutInflator(Context context);
    public String getMetadata(String key);
    public BlinqApplicationFlavour getFlavour();
}
