package it.unimib.pickapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import it.unimib.pickapp.R;
/**
 * It shows the registration second page containing survey about sport and current level.
 */
public class RegistrationSurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_survey);
    }
}