package it.unimib.pickapp.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.pickapp.R;

/**
 * It contains the main fragments of the app.
 */
public class pickappActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickapp);
        toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.pickapp_fragment_container_view);
        if(navHostFragment.getNavController() != null) {
            NavController navController = navHostFragment.getNavController();
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);

            navController.addOnDestinationChangedListener((controller, destination, argument) -> {
                if (destination.getId() == R.id.matchFragment
                        || destination.getId() == R.id.placeFragment
                        || destination.getId() == R.id.FPlaceSelectionFragment
                ) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            });
        }
    }


}