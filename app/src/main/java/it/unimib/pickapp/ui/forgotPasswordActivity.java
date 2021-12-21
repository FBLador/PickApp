package it.unimib.pickapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import it.unimib.pickapp.R;
/**
 * It shows the forgot password page.
 */
public class forgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "forgotPasswordActivity";
    private Button buttonForgotPassword;
    private FirebaseAuth mAuth;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        //listener per button per il login
        buttonForgotPassword = (Button) findViewById(R.id.buttonForgotPasswordSend);
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick Send");

                editTextEmail = findViewById(R.id.editTextEmailForgot);
                String email = editTextEmail.getText().toString();
                if(checkData()) {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                       Toast.makeText(forgotPasswordActivity.this, "Check your email to reset password", Toast.LENGTH_LONG).show();
                                       openLoginActivity();
                                   } else {
                                       Toast.makeText(forgotPasswordActivity.this, "Unable to send reset mail", Toast.LENGTH_LONG).show();
                                   }
                               }
                           });
                }
            }
        });
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, loginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean checkData() {
        String email = editTextEmail.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("email required!");
            editTextEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("enter a valid email!");
            editTextEmail.requestFocus();
            return false;
        }
        return true;
    }
}