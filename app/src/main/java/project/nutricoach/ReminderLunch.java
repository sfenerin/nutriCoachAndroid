package project.nutricoach;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Shawn on 5/26/2017.
 */

public class ReminderLunch extends BroadcastReceiver {

    @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();
            Intent newIntent = new Intent(context, ReminderLunchIS.class);
            Log.d("Notification Builder","launching lunch notification i think?");
            context.startService(newIntent);
        }
}
