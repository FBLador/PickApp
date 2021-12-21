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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.pickapp.R;

/**
 * It shows the login page.
 */
public class loginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Button buttonSignUpFromLogin;
    private Button buttonLogin;
    private TextView textViewForgotPassword;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(currentUser != null){
            openPickappActivity();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //listener per button per il login
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick Login");

                editTextEmail = findViewById(R.id.editTextTextEmailAddress);
                editTextPassword = findViewById(R.id.editTextTextPassword);
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if (checkData()) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        Toast.makeText(loginActivity.this, "login successful", Toast.LENGTH_SHORT).show();

                                        //FirebaseUser user = mAuth.getCurrentUser();
                                        //updateUI(user);
                                        openPickappActivity();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(loginActivity.this, "authentication failed",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }
                                }
                            });
                }
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
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//funziona come finish() per le activity precedenti
        startActivity(intent);
        finish();//non fa tornare indietro alla pagina di login una volta fatto l'accesso
    }

    private void openForgotPasswordActivity() {
        Intent intent = new Intent(this, forgotPasswordActivity.class);
        startActivity(intent);
    }

    private boolean checkData() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

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
        if (password.isEmpty()) {
            editTextPassword.setError("password required!");
            editTextPassword.requestFocus();
            return false;
        }
        return true;
    }
}