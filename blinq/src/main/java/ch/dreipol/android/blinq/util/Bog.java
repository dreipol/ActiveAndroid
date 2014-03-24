package ch.dreipol.android.blinq.util;

import android.util.Log;

/**
 * Blinq Log - BlinqLog - BliLog - Blog - Bog. Even more joyful Java party times.
 */

public class Bog {

    public enum Category {
        NETWORKING, DATABASE, SYSTEM, UI, VIEW
    }

    private static final String TAG = "BLINQ";

    public static int d(Category category, String msg, Throwable tr) {
        return Log.d(TAG, createMessage(category, msg), tr);
    }

    public static int d(Category category, String msg) {
        return Log.d(TAG, createMessage(category, msg));
    }

    public static int v(Category category, String msg, Throwable tr) {
        return Log.v(TAG, createMessage(category, msg), tr);
    }

    public static int v(Category category, String msg) {
        return Log.v(TAG, createMessage(category, msg));
    }

    public static int e(Category category, String msg) {
        return Log.e(TAG, createMessage(category, msg));
    }

    public static int e(Category category, String msg, Throwable tr) {
        return Log.e(TAG, createMessage(category, msg), tr);
    }

    private static String createMessage(Category category, String msg) {
        return "||" + category + ": " + msg;
    }

}

