package it.unimib.pickapp.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;
import it.unimib.pickapp.model.Sport;

public class addMatchActivity extends AppCompatActivity {

    private PlaceViewModel placeViewModel;
    private AddMatchViewModel addMatchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);

        addMatchViewModel = new ViewModelProvider(this).get(AddMatchViewModel.class);
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        Spinner spinner = (Spinner) findViewById(R.id.sportSpinner);
        String[] keys = Sport.names();
        String[] values = getApplicationContext().getResources().getStringArray(R.array.sports);
        EditText titleEditText = findViewById(R.id.editTextTitle);
        EditText locationEditText = findViewById(R.id.editTextLocation);
        EditText dateEditText = findViewById(R.id.editTextDate);
        EditText timeEditText = findViewById(R.id.editTextTime);
        EditText descriptionEditText = findViewById(R.id.editTextDescription);
        EditText costEditText = findViewById(R.id.editTextCost);
        EditText numberOfTeamsEditText = findViewById(R.id.editTextNumberOfTeams);
        SwitchMaterial isPrivateSwitch = findViewById(R.id.privateSwitch);
        Button createButton = findViewById(R.id.createButton);


        List<SpinnerItem> items = new ArrayList<SpinnerItem>();

        for (int i = 0; i < keys.length; i++) {
            items.add(SpinnerItem.create(keys[i], values[i]));
        }

        spinner.setAdapter(new ArrayAdapter<SpinnerItem>(getApplicationContext(),
                R.layout.sport_spinner_item, items));

        findViewById(R.id.selectLocationButton).setOnClickListener((view) -> {
            Fragment placeFragment = new PlaceFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, placeFragment, "PlaceFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        });

        placeViewModel.getSelected().observe(this, selected -> {
            if (selected != null)
                locationEditText.setText(selected.getName());
        });


        createButton.setOnClickListener((view) -> {
            if (placeViewModel.getSelected().getValue() == null) {
                Toast.makeText(addMatchActivity.this,
                        getResources().getString(R.string.choosePlaceMessage), Toast.LENGTH_SHORT).show();
                return;
            }

            String[] splitDate = dateEditText.getText().toString().split(Pattern.quote("/"));
            String[] splitTime = timeEditText.getText().toString().split(Pattern.quote(":"));
            if (splitTime.length != 2 || splitDate.length != 3) {
                Toast.makeText(addMatchActivity.this,
                        getResources().getString(R.string.invalidTimeDate), Toast.LENGTH_SHORT).show();
                return;
            }
            Match match = addMatchViewModel.getMatch();
            // TODO Validity checks
            match.setTitolo(titleEditText.getText().toString());
            match.setSport(((SpinnerItem) spinner.getSelectedItem()).getKey());
            match.setLuogo(placeViewModel.getSelected().getValue().getId());
            // TODO Language support
            match.setDay(Integer.parseInt(splitDate[0]));
            match.setMonth(Integer.parseInt(splitDate[1]));
            match.setYear(Integer.parseInt(splitDate[2]));
            match.setHour(Integer.parseInt(splitTime[0]));
            match.setMinutes(Integer.parseInt(splitTime[1]));
            match.setDescrizione(descriptionEditText.getText().toString());
            match.setCosto(Double.parseDouble(costEditText.getText().toString()));
            match.setNumeroSquadre(Integer.parseInt(numberOfTeamsEditText.getText().toString()));
            match.setPrivate(isPrivateSwitch.isChecked());


            addMatchViewModel.saveMatch();
        });

        addMatchViewModel.status.observe(this, status -> {
            if (status == AddMatchViewModel.Status.SUCCESSFUL) {
                Toast.makeText(addMatchActivity.this,
                        getResources().getString(R.string.addMatchSuccess), Toast.LENGTH_SHORT).show();
                addMatchActivity.this.finish();
            } else if (status == AddMatchViewModel.Status.FAILED) {
                Toast.makeText(addMatchActivity.this,
                        getResources().getString(R.string.addMatchFail), Toast.LENGTH_SHORT).show();
            }

        });

    }


    static class SpinnerItem {

        public final String key;
        public final String value;

        private SpinnerItem(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static SpinnerItem create(String key, String value) {
            return new SpinnerItem(key, value);
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return value;
        }

    }

}