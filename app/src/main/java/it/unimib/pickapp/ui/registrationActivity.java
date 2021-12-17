package it.unimib.pickapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import it.unimib.pickapp.R;
/**
 * It shows the registration first page.
 */
public class registrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    private Button mButtonSingUpNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        //listener per button next
        mButtonSingUpNext = (Button) findViewById(R.id.buttonSingUpNext);
        mButtonSingUpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick Next");
                openRegistrationSurveyActivity();
            }
        });
    }

    private void openRegistrationSurveyActivity(){
        Intent intent = new Intent(this, registrationSurveyActivity.class);
        startActivity(intent);
    }
}