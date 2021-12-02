package it.unimib.pickapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import it.unimib.pickapp.R;
/**
 * It shows the registration first page.
 */
public class RegistrationActivity extends AppCompatActivity {

    private Button mButtonSingUpNext;
    private static final String TAG = "RegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        /*
        //listener per button next
        mButtonSingUpNext = findViewById(R.id.buttonSingUpNext);
        mButtonSingUpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick");
            }
        });*/
    }
}