package project.nutricoach;



        import android.app.Activity;
        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.database.DataSetObserver;
        import android.icu.text.SimpleDateFormat;
        import android.os.Bundle;
        import android.view.KeyEvent;
        import android.view.View;
        import android.widget.AbsListView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.UnsupportedEncodingException;
        import java.text.ParseException;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
        import java.util.concurrent.ExecutionException;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        import java.util.concurrent.Future;




public class MainActivity extends Activity {
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private Button  buttonLogout;
    private boolean left = true;
    private boolean right = false;
    private ExecutorService es;
    private User currentUser;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getCurrentUser();

        this.es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        buttonSend = (Button) findViewById(R.id.send);
        buttonLogout = (Button) findViewById(R.id.logout);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        return sendChatMessage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    sendChatMessage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                logout();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private boolean sendRightMessage(String input) throws UnsupportedEncodingException, JSONException, ExecutionException, InterruptedException {
        Future<String> future = es.submit(new NutriResponse(input, currentUser));
        String response = future.get();

        updateAlarms();

        chatArrayAdapter.add(new ChatMessage(right,response));
        chatText.setText("");

        return true;
    }

    private void updateAlarms(){
        Date cur = new Date(System.currentTimeMillis());
        Calendar curCal = Calendar.getInstance();
        curCal.setTime(cur);

        updateAlarm(cur,"20:00:00",ReminderNight.class);
        updateAlarm(cur,"17:00:00",ReminderDinner.class);
        updateAlarm(cur,"13:00:00",ReminderLunch.class);

    }

    private void updateAlarm(Date cur,String alarmTime,Class reminder) {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Date time1 = null;
        try {
            time1 = new SimpleDateFormat("HH:mm:ss").parse(alarmTime);
        } catch (ParseException e) {e.printStackTrace();}
        if(cur.before(time1)){
            cancelNotification(reminder,alarmManager);
        } else{
            int hour = time1.getHours();
            initNotification(reminder,alarmManager,hour);
        }
    }


    private void cancelNotification(Class reminder, AlarmManager alarmManager){
        Intent intent = new Intent(this, reminder);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(sender);
    }

    private void initNotification(Class reminder,AlarmManager alarmManager,int hour){
        Intent notifyIntent = new Intent(this,reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        android.icu.util.Calendar calendar = initCalendar(hour,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  calendar.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private android.icu.util.Calendar initCalendar(int hour, int minute){
        android.icu.util.Calendar calendar = android.icu.util.Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, hour);
        calendar.set(android.icu.util.Calendar.MINUTE, minute);
        calendar.set(android.icu.util.Calendar.SECOND, 0);
        return calendar;
    }

    private boolean sendChatMessage() throws InterruptedException, ExecutionException, JSONException, UnsupportedEncodingException {
        String value = chatText.getText().toString();
        chatArrayAdapter.add(new ChatMessage(left, value));
        sendRightMessage(value) ;

        chatText.setText("");

        return true;
    }
    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

    }
    private void getCurrentUser(){
        System.out.println("get Current User");
        FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cDatabase= mDatabase.child("users/"+cUser.getUid());


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("here");

                currentUser = dataSnapshot.getValue(User.class);
                if(dataSnapshot.child("dataToday").child("caloriesToday").getValue()!= null) {
                    currentUser.setCaloriesToday(Double.parseDouble(dataSnapshot.child("dataToday").child("caloriesToday").getValue().toString()));
                    currentUser.setProteinToday(Double.parseDouble(dataSnapshot.child("dataToday").child("proteinToday").getValue().toString()));
                    currentUser.setFatToday(Double.parseDouble(dataSnapshot.child("dataToday").child("fatToday").getValue().toString()));
                    currentUser.setCarbsToday(Double.parseDouble(dataSnapshot.child("dataToday").child("carbsToday").getValue().toString()));
                    currentUser.setLastUpdate(Long.parseLong(dataSnapshot.child("dataToday").child("lastUpdate").getValue().toString()));
                }
                ArrayList<FoodDatabase> foodHistory= new ArrayList<FoodDatabase>();
                for(DataSnapshot data : dataSnapshot.child("foodList").getChildren()){
                   foodHistory.add(data.getValue(FoodDatabase.class));
                }
                currentUser.setFoodHistory(foodHistory);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };
        cDatabase.addValueEventListener(userListener);
    }
}