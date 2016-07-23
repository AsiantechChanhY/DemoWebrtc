package com.example.admin.demowebrtc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private static final int CONNECTION_REQUEST = 1;
    private static boolean commandLineRun = false;
    private static final String TAG = "ConnectActivity";

    private String username;
    private String stdByChannel;

    private ImageView mPhone, mVideo;
    private EditText mEnterPhone;
    private ListView mListPhone;
    private TextView mMainUserName;

    private SharedPreferences mSharedPref;

    private String keyprefRoomServerUrl;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("WebRTC");
        getSupportActionBar().hide();

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        mSharedPref = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        if (!mSharedPref.contains(Constants.USER_NAME)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        username = mSharedPref.getString(Constants.USER_NAME, "");
        stdByChannel = username + Constants.STDBY_SUFFIX;

        mListPhone = (ListView) findViewById(R.id.lvlistphopne);
        mListPhone.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mPhone = (ImageView) findViewById(R.id.imgaddphone);
        mPhone.setOnClickListener(connectListener);

//        mVideo = (ImageView) findViewById(R.id.imgvideo);
//        mVideo.setOnClickListener(connectListener);

        mMainUserName = (TextView) findViewById(R.id.main_username);
        mMainUserName.setText(username);

        mEnterPhone = (EditText) findViewById(R.id.edtEnterPhone);
        mEnterPhone.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(
                            TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_DONE) {
                            return true;
                        }
                        return false;
                    }
                });
        mEnterPhone.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private final OnClickListener connectListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean loopback = false;
            if (view.getId() == R.id.imgaddphone) {
                loopback = true;
            }
            commandLineRun = true;
            connectToRoom(loopback,0 );
        }
    };

    private void connectToRoom (boolean loopback, int  runTimeMs){
         String roomId;
        if (loopback) {
            roomId = mEnterPhone.getText().toString();
        } else {
            roomId = getSelectedItem();
            if (roomId == null) {
                roomId = mEnterPhone.getText().toString();
            }
        }

        String roomUrl = mSharedPref.getString(
                keyprefRoomServerUrl,
                getString(R.string.pref_room_server_url_default));

        Log.d(TAG, "Connecting to room " + roomId + " at URL " + roomUrl);
            Uri uri = Uri.parse(roomUrl);
            Intent intent = new Intent(this, CallActivity.class);
            intent.setData(uri);
            intent.putExtra(Constants.EXTRA_ROOMID, roomId);
            intent.putExtra(Constants.EXTRA_LOOPBACK, loopback);
            intent.putExtra(Constants.EXTRA_CMDLINE, commandLineRun);
            intent.putExtra(Constants.EXTRA_RUNTIME, runTimeMs);

            startActivityForResult(intent, CONNECTION_REQUEST);
    }

    private String getSelectedItem() {
        int position = AdapterView.INVALID_POSITION;
        if (mListPhone.getCheckedItemCount() > 0 && adapter.getCount() > 0) {
            position = mListPhone.getCheckedItemPosition();
            if (position >= adapter.getCount()) {
                position = AdapterView.INVALID_POSITION;
            }
        }
        if (position != AdapterView.INVALID_POSITION) {
            return adapter.getItem(position);
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edtEnterPhone){
            String callNum = mEnterPhone.getText().toString();
            if (callNum.isEmpty() || callNum.equals(username)){
                showToast("Enter a valid user ID to call");
                return;
            }
        }
    }
    private void showToast(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
