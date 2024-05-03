package io.tnmma.ezwallet.util;

import android.content.res.Resources;
import android.os.Looper;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class CommonUtil {

    private static final String TAG = CommonUtil.class.getSimpleName();

    public static boolean isOnMainThread() {
        return Looper.getMainLooper().isCurrentThread();
    }

    public static void logIfOnMainThread(String tag) {
        Log.d(tag, "is on main thread = " + CommonUtil.isOnMainThread());
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static Locale getDefaultAppLocale() {
        return Locale.US;
    }

    public static int getScreenWidthByPercent(int percent) {
        return Resources.getSystem().getDisplayMetrics().widthPixels * percent / 100;
    }
}
