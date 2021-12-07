package it.unimib.pickapp;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;

import java.util.Timer;
import java.util.TimerTask;

import it.unimib.pickapp.databinding.ActivityLoadingBinding;
import it.unimib.pickapp.ui.LoginActivity;

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

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                StartHomeActivity();
            }

        }, time);
    }

    private void StartHomeActivity() {
        Intent intent = new Intent( this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}