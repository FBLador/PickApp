package it.unimib.pickapp.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;
import it.unimib.pickapp.model.Sport;

public class addMatchActivity extends AppCompatActivity {

    private PlaceSelectionViewModel placeSelectionViewModel;
    private AddMatchViewModel addMatchViewModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);

        addMatchViewModel = new ViewModelProvider(this).get(AddMatchViewModel.class);
        placeSelectionViewModel = new ViewModelProvider(this).get(PlaceSelectionViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.sportSpinner);
        String[] keys = Sport.names();
        String[] values = getApplicationContext().getResources().getStringArray(R.array.sports);
        EditText titleEditText = findViewById(R.id.titleInput);
        EditText locationEditText = findViewById(R.id.locationInput);
        EditText dateEditText = findViewById(R.id.editTextDate);
        EditText timeEditText = findViewById(R.id.editTextTime);
        EditText descriptionEditText = findViewById(R.id.editTextDescription);
        EditText costEditText = findViewById(R.id.editTextCost);
        EditText numberOfTeamsEditText = findViewById(R.id.editTextNumberOfTeams);
        SwitchMaterial isPrivateSwitch = findViewById(R.id.privateSwitch);
        Button createButton = findViewById(R.id.saveButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button joinButton = findViewById(R.id.joinButton);
        Button leaveButton = findViewById(R.id.leaveButton);


        List<SpinnerItem> items = new ArrayList<SpinnerItem>();

        for (int i = 0; i < keys.length; i++) {
            items.add(SpinnerItem.create(keys[i], values[i]));
        }

        spinner.setAdapter(new ArrayAdapter<SpinnerItem>(getApplicationContext(),
                R.layout.sport_spinner_item, items));

        findViewById(R.id.selectLocationButton).setOnClickListener((view) -> {
            Fragment placeFragment = new PlaceSelectionFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, placeFragment, "PlaceFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        });

        locationEditText.setOnTouchListener(((view, motionEvent) -> true));

        placeSelectionViewModel.getSelected().observe(this, selected -> {
            if (selected != null)
                locationEditText.setText(selected.getName());
        });


        createButton.setOnClickListener((view) -> {
            if (placeSelectionViewModel.getSelected().getValue() == null) {
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
            match.setLuogo(placeSelectionViewModel.getSelected().getValue().getId());
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
                        getResources().getString(R.string.editSuccess), Toast.LENGTH_SHORT).show();
                addMatchActivity.this.finish();
            } else if (status == AddMatchViewModel.Status.FAILED) {
                Toast.makeText(addMatchActivity.this,
                        getResources().getString(R.string.editFailure), Toast.LENGTH_SHORT).show();
            }

        });

/*        MaterialDatePicker.Builder dpBuilder = MaterialDatePicker.Builder.datePicker();
        dpBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = dpBuilder.build();


        dateEditText.setOnClickListener(v -> {
            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });*/


        dateEditText.setOnTouchListener(((view, motionEvent) -> true));

        findViewById(R.id.selectDateButton).setOnClickListener((view) -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            DatePickerDialog picker = new DatePickerDialog(addMatchActivity.this, R.style.DateDialogTheme,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, year, month, day);
            picker.show();
        });


        timeEditText.setOnTouchListener(((view, motionEvent) -> true));

        findViewById(R.id.selectTimeButton).setOnClickListener((view) -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minute = cldr.get(Calendar.MINUTE);
            TimePickerDialog picker;
            picker = new TimePickerDialog(addMatchActivity.this, R.style.TimePickerDialogTheme,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            timeEditText.setText(hour + ":" + minute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
            picker.show();
        });


        if (addMatchViewModel.getMatch().getId() == null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.createGame);
            deleteButton.setVisibility(View.GONE);
        }


//        dateEditText.setOnClickListener(v -> {
//            final Calendar cldr = Calendar.getInstance();
//            int day = cldr.get(Calendar.DAY_OF_MONTH);
//            int month = cldr.get(Calendar.MONTH);
//            int year = cldr.get(Calendar.YEAR);
//            // date picker dialog
//            DatePickerDialog picker = new DatePickerDialog(addMatchActivity.this, R.style.DateDialogTheme,
//                    new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                            dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
//                        }
//                    }, year, month, day);
//            picker.show();
//        });


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