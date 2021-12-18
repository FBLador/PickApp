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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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



        //listener per button finish
        mButtonSingUpFinish = (Button) findViewById(R.id.buttonSingUpFinish);
        mButtonSingUpFinish.setOnClickListener(v -> {
            if(!isExperienceSelected())
                Snackbar.make(findViewById(android.R.id.content), "select your experience level ", Snackbar.LENGTH_SHORT)
                        .show();
            else {
                String favouriteSport = sportSelected();
                String experienceLevel = experienceSelected();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {

                            if(task.isSuccessful()){
                                User user = new User(name, surname, nickname, email, password, favouriteSport, experienceLevel, 0);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(task1 -> {

                                            if(task1.isSuccessful()) {
                                                Snackbar.make(findViewById(android.R.id.content), "registration successful", Snackbar.LENGTH_SHORT)
                                                        .show();
                                                openPickappActivity();
                                            }
                                            else{
                                                Snackbar.make(findViewById(android.R.id.content), "registration failed", Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            }else{
                                Snackbar.make(findViewById(android.R.id.content), "registration failed", Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });
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
}