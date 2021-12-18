package it.unimib.pickapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import it.unimib.pickapp.R;
/**
 * It shows the login page.
 */
public class loginActivity extends AppCompatActivity {

    private static final String TAG = "loginActivity";
    private Button buttonSignUpFromLogin;
    private Button buttonLogin;
    private TextView textViewForgotPassword;

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


        //listener per button per forgot password
        textViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPasswordFromLogin);
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick Forgot");
                openForgotPasswordActivity();
            }
        });
    }


    private void openRegistrationActivity() {
        Intent intent = new Intent(this, registrationActivity.class);
        startActivity(intent);
    }


    private void openPickappActivity() {
        Intent intent = new Intent(this, pickappActivity.class);
        startActivity(intent);
    }

    private void openForgotPasswordActivity() {
        Intent intent = new Intent(this, forgotPasswordActivity.class);
        startActivity(intent);
    }

}