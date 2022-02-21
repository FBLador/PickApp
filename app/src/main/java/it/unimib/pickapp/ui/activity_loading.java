package it.unimib.pickapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

import it.unimib.pickapp.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class activity_loading extends AppCompatActivity {

    long time = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceStace) {
        super.onCreate(savedInstanceStace);
        setContentView(R.layout.activity_loading);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                openLoginActivity();
            }

        }, time);
    }

    private void openLoginActivity() {
        Intent intent = new Intent( this, loginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openPickappActivity() {
        Intent intent = new Intent( this, pickappActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}