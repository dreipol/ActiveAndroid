package ch.dreipol.android.blinq.util;

import rx.Subscription;
/*
A subscription that doe nothing on unsubscribe.
 */
public class NullSubscription implements Subscription {

    private boolean isUnsubscribed = false;

    @Override
    public void unsubscribe() {
        isUnsubscribed = true;
    }

    @Override
    public boolean isUnsubscribed() {
        return isUnsubscribed;
    }

}