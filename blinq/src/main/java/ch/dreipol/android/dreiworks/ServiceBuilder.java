package ch.dreipol.android.dreiworks;

import ch.dreipol.android.blinq.services.impl.BaseService;

/**
 * Created by melbic on 27/08/14.
 */
public class ServiceBuilder<T extends BaseService> {

    private Class<T> mClass;

    public ServiceBuilder(Class<T> clazz) {

        mClass = clazz;
    }

    public T build() throws IllegalAccessException, InstantiationException {

        return mClass.newInstance();
    }
}
