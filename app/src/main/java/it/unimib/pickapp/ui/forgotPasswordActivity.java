package it.unimib.pickapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import it.unimib.pickapp.R;
/**
 * It shows the forgot password page.
 */
public class forgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "forgotPasswordActivity";
    private Button buttonForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //listener per button per il login
        buttonForgotPassword = (Button) findViewById(R.id.buttonForgotPasswordSend);
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick Send");
                openLoginActivity();
            }
        });
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, loginActivity.class);

        // TODO : logica per far spedire messaggio di reset password

        startActivity(intent);
    }
}