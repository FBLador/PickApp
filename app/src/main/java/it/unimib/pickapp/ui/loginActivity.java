package it.unimib.pickapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.pickapp.R;

/**
 * It shows the login page.
 */
public class loginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //riferimenti alle due EditText: mail e password
    private EditText editTextEmail;
    private EditText editTextPassword;
    //oggetto firebase
    private FirebaseAuth mAuth;


    @Override
    //se l'utente è gia loggato lo rimando dirrettamente a PickappActivity
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
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(v -> {
            Log.d(TAG, "onClick Login");
            //recupero mail e password
            editTextEmail = findViewById(R.id.editTextTextEmailAddress);
            editTextPassword = findViewById(R.id.editTextTextPassword);
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            //controlla validità dati
            if (checkData()) {
                //autenticazione con mail e password
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            //tutto ok
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(loginActivity.this, R.string.loginSuccess, Toast.LENGTH_SHORT).show();

                                //FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                //apro PickappActivity
                                openPickappActivity();
                            } else {
                                //stampo errore
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(loginActivity.this, R.string.authenticationFailed,
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        });
            }
        });

        //listener per button per il sign up
        Button buttonSignUpFromLogin = findViewById(R.id.buttonSignUpFromLogin);
        buttonSignUpFromLogin.setOnClickListener(v -> {
            Log.d(TAG, "onClick SignUp");
            openRegistrationActivity();
        });


        //listener per button per forgot password
        TextView textViewForgotPassword = findViewById(R.id.textViewForgotPasswordFromLogin);
        textViewForgotPassword.setOnClickListener(v -> {
            Log.d(TAG, "onClick Forgot");
            openForgotPasswordActivity();
        });
    }


    private void openRegistrationActivity() {
        Intent intent = new Intent(this, registrationActivity.class);
        startActivity(intent);
        finish();
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

    //controllo validità dati inseriti
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