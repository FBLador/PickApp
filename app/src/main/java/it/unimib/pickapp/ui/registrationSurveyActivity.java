package it.unimib.pickapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import it.unimib.pickapp.R;
/**
 * It shows the registration second page containing survey about sport and current level.
 */
public class registrationSurveyActivity extends AppCompatActivity {

    private static final String TAG = "RegistrSurveyActivity";
    private Button mButtonSingUpFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_survey);


        //listener per button finish
        mButtonSingUpFinish = (Button) findViewById(R.id.buttonSingUpFinish);
        mButtonSingUpFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick Finish");
                openPickappActivity();
            }
        });
    }

    private void openPickappActivity(){
        Intent intent = new Intent(this, pickappActivity.class);
        startActivity(intent);
    }
}