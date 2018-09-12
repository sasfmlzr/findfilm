package com.sasfmlzr.findfilm.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.sasfmlzr.findfilm.MainActivity;
import com.sasfmlzr.findfilm.R;

import java.util.concurrent.TimeUnit;

public class NotificationService extends IntentService {
    private static final String BIG_TEXT = "Looking film!";
    private static final String LOW_TEXT = "More, more film!";
    private NotificationManager nm;

    public NotificationService() {
        super("Notify");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendNotification();
    }

    private void sendNotification() {
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ic_app);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent ii = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(LOW_TEXT);
        bigText.setBigContentTitle(BIG_TEXT);

        notifyBuilder
                .setContentIntent(pendingIntent)
                .setContentTitle(BIG_TEXT)
                .setContentText(LOW_TEXT)
                .setStyle(bigText)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_app)
                .setLargeIcon(icon);
        Notification notification = notifyBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    BIG_TEXT,
                    NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
        }
        nm.notify(0, notification);
    }
}
