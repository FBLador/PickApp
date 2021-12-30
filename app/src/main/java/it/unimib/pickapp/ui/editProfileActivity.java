package it.unimib.pickapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import it.unimib.pickapp.R;

/**
 * It contains the page to edit the user profile.
 */
public class editProfileActivity extends AppCompatActivity {

    Button buttonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        buttonDone = this.findViewById(R.id.EditProfileDone);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPickappActivity();
            }
        });

    }

    private void openPickappActivity() {
        Intent intent = new Intent(this, pickappActivity.class);
        startActivity(intent);
    }

}