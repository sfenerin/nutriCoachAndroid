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

 import com.loopj.android.http.JsonHttpResponseHandler;

 import org.json.JSONArray;
 import org.json.JSONException;
 import org.json.JSONObject;

 import cz.msebera.android.httpclient.Header;



public class MainActivity extends Activity {
    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean left = true;
    private boolean right = false;

    NutriResponse nutritionProcess;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        nutritionProcess = new NutriResponse();

        buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
                sendRightMessage();
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

    private boolean sendRightMessage() {
        String response = nutritionProcess.process(chatText.getText().toString());
        chatArrayAdapter.add(new ChatMessage(right,response));
        chatText.setText("");
        return true;
    }

    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(left, chatText.getText().toString()));
        chatText.setText("");
        return true;
     }

    }
