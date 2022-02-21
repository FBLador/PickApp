package it.unimib.pickapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Sport;
import it.unimib.pickapp.model.User;

/**
 * It shows the registration second page containing survey about sport and current level.
 */
public class registrationSurveyActivity extends AppCompatActivity {

    private static final String TAG = "RegistrSurveyActivity";
    //riferimento per il bottone finish
    private Button mButtonSingUpFinish;

    //riferimenti allo Spinner al RadioGroup e ai RadioButton
    private Spinner mSpinnerSport;
    private RadioGroup mRadioGroupExperience;
    private RadioButton mRadioButtonExperience;

    //oggetti firebase
    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_survey);

        mSpinnerSport = findViewById(R.id.spinner_sport);
        String[] keys = Sport.names();
        String[] values = this.getApplicationContext()
                .getResources().getStringArray(R.array.sports);
        List<MatchFragment.SpinnerItem> items = new ArrayList<>();

        for (int i = 0; i < keys.length; i++) {
            items.add(MatchFragment.SpinnerItem.create(keys[i], values[i]));
        }
        mSpinnerSport.setAdapter(new ArrayAdapter<>(
                this.getApplicationContext(),
                R.layout.sport_spinner_item, items));

        mAuth = FirebaseAuth.getInstance();

        //recupero le informazione passate da registrationActivity
        String name = getIntent().getStringExtra("keyname");
        String surname = getIntent().getStringExtra("keysurname");
        String nickname = getIntent().getStringExtra("keynickname");
        String email = getIntent().getStringExtra("keyemail");
        String password = getIntent().getStringExtra("keypassword");

        //firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //listener per button finish
        mButtonSingUpFinish = (Button) findViewById(R.id.buttonSingUpFinish);
        //se il bottone finish viene schiacciato
        mButtonSingUpFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick Finish");
                //se non è stato selezionato un livello di esperienza
                if(!isExperienceSelected()) {
                    Toast.makeText(registrationSurveyActivity.this, "select your experiance level", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "if esterno");
                }
                //livello di esperienza selezionato, tutto ok
                else {
                    //recupero sport e livello di esperienza
                    String favouriteSport = sportSelected().toUpperCase(Locale.ROOT);
                    String experienceLevel = experienceSelected();

                    //creo un nuvo utente e lo aggiungo alla sezione FirebaseAuth
                    mAuth.createUserWithEmailAndPassword(email, password)
                         .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       Log.d(TAG, "if interno");
                                       double experienceLevelDouble = 0;
                                       //creo l'utente
                                       if (experienceLevel.equals("Beginner"))
                                           experienceLevelDouble = 0;
                                       if (experienceLevel.equals("Intermediate"))
                                           experienceLevelDouble = 2.5;
                                       if (experienceLevel.equals("Advanced"))
                                           experienceLevelDouble = 5;
                                       user = new User(name, surname, nickname, email, password, favouriteSport, experienceLevelDouble, 2.5);
                                       Toast.makeText(registrationSurveyActivity.this, "Registration successful ", Toast.LENGTH_SHORT).show();
                                       Log.d(TAG, "FINO A QUI TUTTO BENE");
                                       //aggiungo l'utente a firebase db
                                       addDataToFirebase();
                                       //apro pickappActivity
                                       openPickappActivity();
                                   }else{
                                       //registrazione fallita
                                       Toast.makeText(registrationSurveyActivity.this, "Registration failed ", Toast.LENGTH_SHORT).show();
                                       Log.d(TAG, "else esterno");
                                   }
                                }
                            });
                }
            }
        });
    }

    private void openPickappActivity(){
        //Apro l'activity pickappActivity
        Intent intent = new Intent(this, pickappActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//funziona come finish() per le activity precedenti
        startActivity(intent);
        finish();
    }

    private String sportSelected(){
        //ritorna lo sport selezionato
        mSpinnerSport = findViewById(R.id.spinner_sport);
        String sport = mSpinnerSport.getSelectedItem().toString();
        return sport;
    }

    //ritona true sse è stato selezionato un livello di esperienza
    private boolean isExperienceSelected(){
        mRadioGroupExperience = findViewById(R.id.radioGroup_experience);
        return mRadioGroupExperience.getCheckedRadioButtonId() != -1;
    }

    //ritorna il livello di esperienza
    private String experienceSelected(){
        mRadioGroupExperience = findViewById(R.id.radioGroup_experience);
        int radioButtonID = mRadioGroupExperience.getCheckedRadioButtonId();
        mRadioButtonExperience = mRadioGroupExperience.findViewById(radioButtonID);
        String experience = mRadioButtonExperience.getText().toString();
        return experience;
    }

    //metodo per aggiungere l'utente al real-time db
    private void addDataToFirebase() {
        //add user to realtime database firebase
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, currentFirebaseUser.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "QUA VA COSì COSì");
                //aggiungo l'utente al path "Users" con chiave Uid dell'autenticazione
                myRef.child(currentFirebaseUser.getUid()).setValue(user);
                Toast.makeText(registrationSurveyActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(registrationSurveyActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
