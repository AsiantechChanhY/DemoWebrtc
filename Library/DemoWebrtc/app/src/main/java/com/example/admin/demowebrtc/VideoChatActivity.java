package com.example.admin.demowebrtc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Admin on 7/23/2016.
 */
public class VideoChatActivity extends Activity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);

        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(Constants.EXTRA_ROOMID)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Need to pass username to VideoChatActivity in intent extras (Constants.USER_NAME).",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        username = extras.getString(Constants.EXTRA_ROOMID, "");


    }
}
