package ch.dreipol.android.blinq.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.dreipol.android.blinq.services.model.LoadingInfo;
import ch.dreipol.android.blinq.ui.viewgroups.DrawerPosition;
import rx.Observable;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;

/**
 * Created by phil on 24/08/14.
 */
public abstract class BlinqFragment extends Fragment implements IDrawerPositionListener{
    protected BehaviorSubject<View> mViewSubject;
    protected BehaviorSubject<LoadingInfo> mDataSubject;
    protected Observable<LoadingInfo> mLoadingSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewSubject = BehaviorSubject.create();
        mDataSubject = BehaviorSubject.create();

        mLoadingSubscription = Observable.zip(mViewSubject, mDataSubject, new Func2<View, LoadingInfo, LoadingInfo>() {
            @Override
            public LoadingInfo call(View view, LoadingInfo loadingInfo) {
                loadingInfo.setViewContainer(view);
                return loadingInfo;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        mViewSubject.onNext(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mViewSubject = null;
//        mDataSubject = null;
    }

    protected abstract int getLayoutResourceId();

    @Override
    public void setPosition(DrawerPosition newPosition) {
//        View view = getView();
//        if(view instanceof ViewGroup){
//            disableEnableControls(newPosition.equals(DrawerPosition.CENTER), (ViewGroup) view);
//        }


    }
//    private void disableEnableControls(boolean enable, ViewGroup vg){
//        for (int i = 0; i < vg.getChildCount(); i++){
//            View child = vg.getChildAt(i);
//            child.setEnabled(enable);
//            if (child instanceof ViewGroup){
//                disableEnableControls(enable, (ViewGroup)child);
//            }
//        }
//    }

}
