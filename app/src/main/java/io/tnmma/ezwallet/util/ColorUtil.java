package io.tnmma.ezwallet.util;

import androidx.core.graphics.ColorUtils;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.IvyApplication;

public class ColorUtil {

    private static final String TAG = ColorUtil.class.getSimpleName();

    private static int ivyItemBackgroundColor;

    private static int ivyItemForegroundColor;

    public static int getColorFromResId(int colorResId) {
        return IvyApplication.getInstance().getColor(colorResId);
    }

    public static boolean isDarkColor(int color) {
        return (ColorUtils.calculateLuminance(color) <= 0.5);
    }

    public static int getItemForegroundColor(int backgroundColor) {
        return getColorFromResId(
                isDarkColor(backgroundColor) ? R.color.ivy_white : R.color.ivy_black);
    }

    public static int getColorWithAlpha(int color, int alphaPercent) {
        return ColorUtils.setAlphaComponent(color, alphaPercent * 0xFF / 100);
    }

    public static int getIvyItemBackgroundColor() {
        return ivyItemBackgroundColor;
    }

    public static int getIvyItemForegroundColor() {
        return ivyItemForegroundColor;
    }

    public static void setIvyItemBackgroundColor(int color) {
        ivyItemBackgroundColor = color;
    }

    public static void setIvyItemForegroundColor(int color) {
        ivyItemForegroundColor = color;
    }
}
