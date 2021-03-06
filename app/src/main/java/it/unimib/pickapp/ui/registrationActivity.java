package it.unimib.pickapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import it.unimib.pickapp.R;

/**
 * It shows the registration first page.
 */
public class registrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    //riferimento al bottone next
    private Button mButtonSingUpNext;

    //oggetto per Firebase
    private FirebaseAuth mAuth;
    //riferimenti alle EditText
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextNickname;
    private EditText editTextEmail;
    private EditText editTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //recupero le info inserite dall'utente
        editTextName = findViewById(R.id.editTextNameAdd);
        editTextSurname = findViewById(R.id.editTextSurnameAdd);
        editTextNickname = findViewById(R.id.editTextNicknameAdd);
        editTextEmail = findViewById(R.id.editTextEmailAdd);
        editTextPassword = findViewById(R.id.editTextPasswordAdd);

        mButtonSingUpNext = findViewById(R.id.buttonSingUpNext);
        //se l'utente schiaccia il bottone next
        mButtonSingUpNext.setOnClickListener(v -> {
            Log.d(TAG, "onClick Next");
            //controllo che i dati siano validi
            if (checkData())
                //apro la schermata per completare la registrazione
                openRegistrationSurveyActivity();
        });
    }

    private void openRegistrationSurveyActivity(){
        Intent intent = new Intent(this, registrationSurveyActivity.class);

        //mando i dati a registrationSurveyActivity con il metodo putExtra
        intent.putExtra("keyname", editTextName.getText().toString());
        intent.putExtra("keysurname", editTextSurname.getText().toString());
        intent.putExtra("keynickname", editTextNickname.getText().toString());
        intent.putExtra("keyemail", editTextEmail.getText().toString());
        intent.putExtra("keypassword", editTextPassword.getText().toString());

        //rimando a registrationSurveyActivity
        startActivity(intent);
        finish();
    }

    private boolean checkData(){
        //recupero i dati e li controllo che siano validi
        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String nickname = editTextNickname.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(name.isEmpty()){
            editTextName.setError("name required!");
            editTextName.requestFocus();
            return false;
        }
        if(surname.isEmpty()){
            editTextSurname.setError("surname required!");
            editTextSurname.requestFocus();
            return false;
        }
        if(nickname.isEmpty()){
            editTextNickname.setError("nickname required!");
            editTextNickname.requestFocus();
            return false;
        }
        if(email.isEmpty()){
            editTextEmail.setError("email required!");
            editTextEmail.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("enter a valid email!");
            editTextEmail.requestFocus();
            return false;
        }
        if(password.isEmpty()){
            editTextPassword.setError("password required!");
            editTextPassword.requestFocus();
            return false;
        }
        if(password.length() < 6){
            editTextPassword.setError("a password of at least 6 characters is required!");
            editTextPassword.requestFocus();
            return false;
        }
        return true;
    }
}