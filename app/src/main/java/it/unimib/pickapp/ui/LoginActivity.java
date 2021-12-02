package it.unimib.pickapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import it.unimib.pickapp.R;
/**
 * It shows the login page.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}