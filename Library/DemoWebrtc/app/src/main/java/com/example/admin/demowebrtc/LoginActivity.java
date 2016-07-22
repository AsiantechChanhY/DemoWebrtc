package com.example.admin.demowebrtc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Admin on 7/22/2016.
 */
public class LoginActivity extends Activity implements View.OnClickListener{
    Button mLogin;
    EditText mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mLogin = (Button) findViewById(R.id.btnlogin);
        mLogin.setOnClickListener(this);

        mUsername = (EditText) findViewById(R.id.edtusername);

        Bundle extras  = getIntent().getExtras();
        if (extras != null){
            String lastUsername = extras.getString("oldUsername", "");
            mUsername.setText(lastUsername);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnlogin) {
            String username = mUsername.getText().toString();

            if (!validUsername(username))
                return;

                SharedPreferences sp = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constants.USER_NAME, username);
                editor.apply();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }
    }

    private boolean validUsername(String username) {
        if (username.length() == 0) {
            mUsername.setError("Username cannot be empty.");
            return false;
        }
        if (username.length() > 16) {
            mUsername.setError("Username too long.");
            return false;
        }
        return true;
    }
}
