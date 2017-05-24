package project.nutricoach;



        import android.app.Activity;
        import android.database.DataSetObserver;
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
        import java.util.ArrayList;
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
        String response = "";


        response = future.get();

        chatArrayAdapter.add(new ChatMessage(right,response));

        chatText.setText("");
        return true;
    }

    private boolean sendChatMessage() throws InterruptedException, ExecutionException, JSONException, UnsupportedEncodingException {
        String value = chatText.getText().toString();
        chatArrayAdapter.add(new ChatMessage(left, value));
        sendRightMessage(value) ;

        chatText.setText("");

        return true;
    }
    private void getCurrentUser(){
        System.out.println("get Current USer");
        FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cDatabase= mDatabase.child("users/"+cUser.getUid());


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("here");

                // Get Post object and use the values to update the UI
                currentUser = dataSnapshot.getValue(User.class);

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        cDatabase.addValueEventListener(userListener);
    }
}