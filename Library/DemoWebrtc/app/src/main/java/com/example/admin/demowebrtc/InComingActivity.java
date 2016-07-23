package com.example.admin.demowebrtc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Admin on 7/23/2016.
 */
public class InComingActivity extends Activity {
    private SharedPreferences mSharedPref;
    private TextView mUsername;
    private ImageButton mCallConnect;

    private static boolean commandLineRun = false;
    private static final int CONNECTION_REQUEST = 1;

    private String username;
    private String callUser;

    private String keyprefRoomServerUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incomingcall_activity);

        mCallConnect = (ImageButton) findViewById(R.id.btnconect);
        mCallConnect.setOnClickListener(connect);

        this.mSharedPref = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        if (!this.mSharedPref.contains(Constants.USER_NAME)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        username = mSharedPref.getString(Constants.EXTRA_ROOMID,"");

        Bundle extras = getIntent().getExtras();
        if (extras==null || !extras.containsKey(Constants.EXTRA_ROOMID)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Need to pass username to IncomingCallActivity in intent extras (Constants.CALL_USER).",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        callUser = extras.getString(Constants.EXTRA_ROOMID, "");
        mUsername = (TextView) findViewById(R.id.caller_id);
        mUsername.setText(callUser);
    }

    private final View.OnClickListener connect = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean loopback = false;
            if (view.getId() == R.id.imgvideo) {
                loopback = true;
            }
            commandLineRun = true;
            acceptCall(loopback, 0 );
        }
    };

    public void acceptCall( boolean loopback,int runTimeMs){

        String roomUrl = mSharedPref.getString(
                keyprefRoomServerUrl,
                getString(R.string.pref_room_server_url_default));

        Uri uri = Uri.parse(roomUrl);

        Intent intent = new Intent(InComingActivity.this, CallActivity.class);
        intent.setData(uri);
        intent.putExtra(Constants.EXTRA_ROOMID, callUser);
        intent.putExtra(Constants.EXTRA_LOOPBACK, loopback);
        intent.putExtra(Constants.EXTRA_CMDLINE, commandLineRun);
        intent.putExtra(Constants.EXTRA_RUNTIME, runTimeMs);
        startActivityForResult(intent, CONNECTION_REQUEST);
    }

    public void rejectCall(View view){
                Intent intent = new Intent(InComingActivity.this, MainActivity.class);
                startActivity(intent);
    }

}
