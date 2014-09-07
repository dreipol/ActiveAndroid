package ch.dreipol.android.blinq.services.model;

import android.view.View;

import ch.dreipol.android.blinq.ui.fragments.LoadingState;

/**
 * Created by phil on 21/08/14.
 */
public class LoadingInfo<T extends ILoadable> {

    private final LoadingState mState;
    private T mData;
    private View mViewContainer;

    public LoadingInfo(LoadingState mState) {
        this.mState = mState;
    }

    public T getData() {
        return mData;
    }

    public void setData(T loadableData) {
        this.mData = loadableData;
    }

    public LoadingState getState() {
        return mState;
    }

    public View getViewContainer() {
        return mViewContainer;
    }

    public void setViewContainer(View viewContainer) {
        mViewContainer = viewContainer;
    }
}