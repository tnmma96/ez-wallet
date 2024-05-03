package io.tnmma.ezwallet.util;

import io.tnmma.ezwallet.base.IvyApplication;

public class StringUtil {

    private static final String TAG = StringUtil.class.getSimpleName();

    public static String getStringFromResId(int stringResId) {
        return IvyApplication.getInstance().getString(stringResId);
    }
}
