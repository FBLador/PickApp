package it.unimib.pickapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import it.unimib.pickapp.R;
/**
 * It shows the login page.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Button buttonSignUpFromLogin;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //listener per button per il login
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick Login");
                openPickappActivity();
            }
        });


        //listener per button per il sign up
        buttonSignUpFromLogin = (Button) findViewById(R.id.buttonSignUpFromLogin);
        buttonSignUpFromLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick SignUp");
                openRegistrationActivity();
            }
        });
    }


    private void openRegistrationActivity() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }


    private void openPickappActivity() {
        Intent intent = new Intent(this, PickappActivity.class);
        startActivity(intent);
    }

}