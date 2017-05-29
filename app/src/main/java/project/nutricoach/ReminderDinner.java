package project.nutricoach;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import project.nutricoach.ReminderLunchIS;

public class ReminderDinner extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();
        Intent newIntent = new Intent(context, ReminderDinnerRecIS.class);
        Log.d("Notification Builder","launching dinner notification i think?");
        context.startService(newIntent);
    }
}
