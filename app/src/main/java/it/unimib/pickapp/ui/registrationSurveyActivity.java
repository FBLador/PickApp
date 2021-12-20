package it.unimib.pickapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.User;

/**
 * It shows the registration second page containing survey about sport and current level.
 */
public class registrationSurveyActivity extends AppCompatActivity {

    private static final String TAG = "RegistrSurveyActivity";
    private Button mButtonSingUpFinish;

    private Spinner mSpinnerSport;
    private RadioGroup mRadioGroupExperience;
    private RadioButton mRadioButtonExperience;
    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_survey);

        mAuth = FirebaseAuth.getInstance();

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
        mButtonSingUpFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick Finish");
                openPickappActivity();
                if(!isExperienceSelected()) {
                    Snackbar.make(findViewById(android.R.id.content), "select your experience level ", Snackbar.LENGTH_SHORT)
                            .show();
                    Log.d(TAG, "if esterno");
                }
                else {
                    String favouriteSport = sportSelected();
                    String experienceLevel = experienceSelected();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        user = new User(name, surname, nickname, email, password, favouriteSport, experienceLevel, 2.5);
                                        Snackbar.make(findViewById(android.R.id.content), "registration successful", Snackbar.LENGTH_SHORT)
                                                .show();
                                        addDatatoFirebase();
                                        openPickappActivity();
                                        Log.d(TAG, "if interno");
                                    }else{
                                        Snackbar.make(findViewById(android.R.id.content), "registration failed", Snackbar.LENGTH_SHORT)
                                                .show();
                                        Log.d(TAG, "else esterno");
                                    }
                                }
                            });
                }
            }
        });
    }

    private void openPickappActivity(){
        Intent intent = new Intent(this, pickappActivity.class);
        startActivity(intent);
    }

    private String sportSelected(){
        mSpinnerSport = findViewById(R.id.spinner_sport);
        String sport = mSpinnerSport.getSelectedItem().toString();
        return sport;
    }

    private boolean isExperienceSelected(){
        mRadioGroupExperience = findViewById(R.id.radioGroup_experience);
        if(mRadioGroupExperience.getCheckedRadioButtonId() == -1)
            return false;
        return true;
    }

    private String experienceSelected(){
        mRadioGroupExperience = findViewById(R.id.radioGroup_experience);
        int radioButtonID = mRadioGroupExperience.getCheckedRadioButtonId();
        mRadioButtonExperience = mRadioGroupExperience.findViewById(radioButtonID);
        String experience = mRadioButtonExperience.getText().toString();
        return experience;
    }

    private void addDatatoFirebase() {
        //add user to realtime database firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(user.getNickname()).setValue(user);

                Toast.makeText(registrationSurveyActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(registrationSurveyActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}