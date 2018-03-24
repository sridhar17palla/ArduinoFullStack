package com.nextlevel.playarduino.arduinofullstack.Utility;

import android.content.res.Resources;

/**
 * Created by sukumar on 8/28/17.
 */

public class Utils {

    //TODO: need to set a boolean which confirms whether the current running device is LinkerDevice
    public static boolean isThisLinkerDevice = false;

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
