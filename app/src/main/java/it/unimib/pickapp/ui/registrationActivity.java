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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.pickapp.R;
/**
 * It shows the registration first page.
 */
public class registrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    private Button mButtonSingUpNext;

    private FirebaseAuth mAuth;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextNickname;
    private EditText editTextEmail;
    private EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextName = findViewById(R.id.editTextNameAdd);
        editTextSurname = findViewById(R.id.editTextSurnameAdd);
        editTextNickname = findViewById(R.id.editTextNicknameAdd);
        editTextEmail = findViewById(R.id.editTextEmailAdd);
        editTextPassword = findViewById(R.id.editTextPasswordAdd);

        mButtonSingUpNext = (Button) findViewById(R.id.buttonSingUpNext);
        mButtonSingUpNext.setOnClickListener(v -> {
            Log.d(TAG, "onClick Next");
            checkData();
            openRegistrationSurveyActivity();
        });

    }


    private void openRegistrationSurveyActivity(){
        Intent intent = new Intent(this, registrationSurveyActivity.class);

        intent.putExtra("keyname", editTextName.getText().toString());
        intent.putExtra("keysurname", editTextSurname.getText().toString());
        intent.putExtra("keynickname", editTextNickname.getText().toString());
        intent.putExtra("keyemail", editTextEmail.getText().toString());
        intent.putExtra("keypassword", editTextPassword.getText().toString());

        startActivity(intent);
    }

    private void checkData(){
        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String nickname = editTextNickname.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(name.isEmpty()){
            editTextName.setError("name required!");
            editTextName.requestFocus();
            return;
        }
        if(surname.isEmpty()){
            editTextSurname.setError("surname required!");
            editTextSurname.requestFocus();
            return;
        }
        if(nickname.isEmpty()){
            editTextNickname.setError("nickname required!");
            editTextNickname.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("email required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("password required!");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("a password of at least 6 characters is required!");
            editTextPassword.requestFocus();
            return;
        }
    }
}