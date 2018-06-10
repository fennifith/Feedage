package me.jfenn.feedage.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ServiceUtils {

    public static void startService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intent);
        else context.startService(intent);
        Log.d("Started Service", "");
    }

}
