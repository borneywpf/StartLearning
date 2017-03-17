package com.study.utils;

import android.content.Context;

/**
 * Created by borney on 3/17/17.
 */

public class DisplayUtils {
    public static int dp2px(Context context, int dpValue) {
        double scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dpValue * (scale)) + .5);
    }

    public static int px2dp(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
