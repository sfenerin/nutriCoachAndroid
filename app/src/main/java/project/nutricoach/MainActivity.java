package project.nutricoach;



        import android.app.Activity;
        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.database.DataSetObserver;
        import android.icu.text.SimpleDateFormat;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.widget.AbsListView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.Toolbar;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

//        import org.json.JSONArray;
        import org.json.JSONException;
//        import org.json.JSONObject;

        import java.io.UnsupportedEncodingException;
        import java.text.ParseException;
//        import java.util.ArrayList;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
//        import java.util.List;
        import java.util.concurrent.ExecutionException;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        import java.util.concurrent.Future;




public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageView buttonSend;
    private Button  buttonLogout;
    private boolean left = true;
    private boolean right = false;
    private ExecutorService es;
    private User currentUser;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        updateMessages();
        getCurrentUser();


        this.es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        buttonSend = (ImageView) findViewById(R.id.send);
        buttonLogout = (Button) findViewById(R.id.logout);

        listView = (ListView) findViewById(R.id.msgview);

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

        ImageView icon= (ImageView) findViewById(R.id.icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
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
        currentUser.addMessage(new ChatMessage(right,response));
        chatArrayAdapter.add(new ChatMessage(right,response));
        chatText.setText("");

        return true;
    }

    private void updateAlarms(){
        Date cur = new Date(System.currentTimeMillis());
        Calendar curCal = Calendar.getInstance();
        curCal.setTime(cur);

        updateAlarm(cur,"20:00:00",ReminderNight.class,2);
        updateAlarm(cur,"17:00:00",ReminderDinner.class,1);
        updateAlarm(cur,"13:00:00",ReminderLunch.class,0);
        Log.d("Notifications","updatd Alarms");
    }

    private void updateAlarm(Date cur,String alarmTime,Class reminder,int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Date time1 = null;
        try {
            time1 = new SimpleDateFormat("HH:mm:ss").parse(alarmTime);
        } catch (ParseException e) {e.printStackTrace();}
        if(cur.before(time1)){
            cancelNotification(reminder,alarmManager);
        } else{
            int hour = time1.getHours();
            initNotification(reminder,alarmManager,hour,requestCode);
        }
    }


    private void cancelNotification(Class reminder, AlarmManager alarmManager){
        Intent intent = new Intent(this, reminder);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(sender);
    }

    private void initNotification(Class reminder,AlarmManager alarmManager,int hour,int requestCode){
        Intent notifyIntent = new Intent(this,reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        currentUser.addMessage(new ChatMessage(left,value));
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };
        cDatabase.addValueEventListener(userListener);
    }

    private void updateMessages(){
        System.out.println("get Current User");
        FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cDatabase= mDatabase.child("users/"+cUser.getUid());


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name= dataSnapshot.child("name").getValue().toString();
                System.out.println("here");
                for(DataSnapshot data: dataSnapshot.child("messages").getChildren()){
                    chatArrayAdapter.add(data.getValue(ChatMessage.class));
                }
                chatArrayAdapter.add(new ChatMessage(right,"Hi " + name + ", what have you eaten today?"));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };
        cDatabase.addListenerForSingleValueEvent(userListener);
    }


}