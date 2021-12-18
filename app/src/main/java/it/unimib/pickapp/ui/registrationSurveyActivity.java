package it.unimib.pickapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import it.unimib.pickapp.R;
/**
 * It shows the registration second page containing survey about sport and current level.
 */
public class registrationSurveyActivity extends AppCompatActivity {

    private static final String TAG = "RegistrSurveyActivity";
    private Button mButtonSingUpFinish;
    private Spinner mSpinnerSport;
    private RadioGroup mRadioGroupExperience;
    private RadioButton mRadioButtonExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_survey);


        //listener per button finish
        mButtonSingUpFinish = (Button) findViewById(R.id.buttonSingUpFinish);
        mButtonSingUpFinish.setOnClickListener(v -> {
            Log.d(TAG, sportSelected());
            if(isExperienceSelected())
                Log.d(TAG, experienceSelected());
            else
                Log.d(TAG, "Experience not selected");
            //Log.d(TAG, "onClick Finish");
            //openPickappActivity();
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