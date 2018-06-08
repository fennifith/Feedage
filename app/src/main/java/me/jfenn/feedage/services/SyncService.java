package me.jfenn.feedage.services;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.List;

import me.jfenn.feedage.Feedage;
import me.jfenn.feedage.R;
import me.jfenn.feedage.activities.SettingsActivity;
import me.jfenn.feedage.lib.FeedageLib;
import me.jfenn.feedage.lib.data.CategoryData;
import me.jfenn.feedage.lib.data.FeedData;

public class SyncService extends Service implements FeedageLib.OnCategoriesUpdatedListener {

    public static final String EXTRA_FORCE_SYNC = "me.jfenn.feedage.EXTRA_FORCE_SYNC";

    private Feedage feedage;
    private boolean isForeground;

    @Override
    public void onCreate() {
        super.onCreate();
        feedage = (Feedage) getApplicationContext();
        feedage.addListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            Calendar now = Calendar.getInstance();
            int syncTime = feedage.getSyncTime();
            boolean shouldSync = now.get(Calendar.HOUR_OF_DAY) == syncTime;

            now.set(Calendar.HOUR_OF_DAY, syncTime);
            now.set(Calendar.MINUTE, 0);
            now.add(Calendar.DAY_OF_YEAR, 1);

            alarmManager.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), PendingIntent.getService(this, 0,
                    new Intent(this, SyncService.class), 0));

            if (intent != null && intent.hasExtra(EXTRA_FORCE_SYNC))
                shouldSync = shouldSync || intent.getBooleanExtra(EXTRA_FORCE_SYNC, false);

            if (!shouldSync) {
                stop();
                return START_NOT_STICKY;
            }
        }

        if (!feedage.isLoading() || !isForeground) {
            feedage.getNext();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (notificationManager != null)
                    notificationManager.createNotificationChannel(new NotificationChannel("sync", getString(R.string.title_articles_sync), NotificationManager.IMPORTANCE_MIN));
            }

            isForeground = true;
            startForeground(723, new NotificationCompat.Builder(this, "sync")
                    .setSmallIcon(R.drawable.ic_notification_sync)
                    .setContentTitle(getString(R.string.title_syncing_articles))
                    .setContentText(" ")
                    .setProgress(0, 0, true)
                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, SettingsActivity.class), 0))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void stop() {
        isForeground = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            stopSelf();

        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onFeedsUpdated(List<FeedData> feeds) {
    }

    @Override
    public void onCategoriesUpdated(List<CategoryData> categories) {
        if (!feedage.isLoading())
            stop();
    }
}
