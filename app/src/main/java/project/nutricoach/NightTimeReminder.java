package project.nutricoach;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import static project.nutricoach.R.drawable.nutrilogo;

/**
 * Created by Shawn on 5/26/2017.
 */

public class NightTimeReminder extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public NightTimeReminder() {
        super("NightTimeReminder");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Don't forget to tell us about the food you had today");
        builder.setContentText("You don't want to lose your streak! You've been doing so well");
        builder.setSmallIcon(nutrilogo);
        builder.setContentIntent(pendingIntent);

        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);

        Log.d("Notification Builder","built the notification");
    }
}
