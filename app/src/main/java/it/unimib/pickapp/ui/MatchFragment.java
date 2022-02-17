package it.unimib.pickapp.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;
import it.unimib.pickapp.model.Sport;

public class MatchFragment extends Fragment {

    private PlaceViewModel placeViewModel;
    private MatchViewModel matchViewModel;

    public static MatchFragment newInstance() {
        return new MatchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarHome);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
        placeViewModel = new ViewModelProvider(requireActivity()).get(PlaceViewModel.class);

        Spinner sportSpinner = (Spinner) view.findViewById(R.id.sportSpinner);
        String[] keys = Sport.names();
        String[] values = requireContext().getApplicationContext()
                .getResources().getStringArray(R.array.sports);
        EditText titleEditText = view.findViewById(R.id.titleInput);
        EditText locationEditText = view.findViewById(R.id.locationInput);
        EditText dateEditText = view.findViewById(R.id.editTextDate);
        EditText timeEditText = view.findViewById(R.id.editTextTime);
        EditText descriptionEditText = view.findViewById(R.id.editTextDescription);
        EditText costEditText = view.findViewById(R.id.editTextCost);
        EditText numberOfTeamsEditText = view.findViewById(R.id.editTextNumberOfTeams);
        SwitchMaterial isPrivateSwitch = view.findViewById(R.id.privateSwitch);
        Button saveButton = view.findViewById(R.id.saveButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);
        Button joinButton = view.findViewById(R.id.joinButton);
        Button leaveButton = view.findViewById(R.id.leaveButton);
        Button selectLocationButton = view.findViewById(R.id.selectLocationButton);
        Button selectDateButton = view.findViewById(R.id.selectDateButton);
        Button selectTimeButton = view.findViewById(R.id.selectTimeButton);

        List<addMatchActivity.SpinnerItem> items = new ArrayList<addMatchActivity.SpinnerItem>();

        for (int i = 0; i < keys.length; i++) {
            items.add(addMatchActivity.SpinnerItem.create(keys[i], values[i]));
        }

        sportSpinner.setAdapter(new ArrayAdapter<addMatchActivity.SpinnerItem>(
                requireContext().getApplicationContext(),
                R.layout.sport_spinner_item, items));

        selectLocationButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_select_match_to_place);
        });

        locationEditText.setOnTouchListener(((View v, MotionEvent motionEvent) -> true));

        placeViewModel.getSelected().observe(getViewLifecycleOwner(), selected -> {
            if (selected != null)
                locationEditText.setText(selected.getName());
        });


        saveButton.setOnClickListener(v -> {
            if (placeViewModel.getSelected().getValue() == null) {
                Toast.makeText(requireContext(),
                        getResources().getString(R.string.choosePlaceMessage), Toast.LENGTH_SHORT).show();
                return;
            }

            String[] splitDate = dateEditText.getText().toString().split(Pattern.quote("/"));
            String[] splitTime = timeEditText.getText().toString().split(Pattern.quote(":"));
            if (splitTime.length != 2 || splitDate.length != 3) {
                Toast.makeText(requireContext(),
                        getResources().getString(R.string.invalidTimeDate), Toast.LENGTH_SHORT).show();
                return;
            }
            Match match = matchViewModel.getMatch();
            // TODO Validity checks
            match.setTitolo(titleEditText.getText().toString());
            match.setSport(((addMatchActivity.SpinnerItem) sportSpinner.getSelectedItem()).getKey());
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


            matchViewModel.saveMatch();
        });

        joinButton.setOnClickListener(v -> {
            matchViewModel.joinMatch();
        });

        leaveButton.setOnClickListener(v -> {
            matchViewModel.leaveMatch();
        });

        matchViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status == AddMatchViewModel.Status.SUCCESSFUL) {
                Toast.makeText(requireContext(),
                        getResources().getString(R.string.editSuccess), Toast.LENGTH_SHORT).show();
                NavController navController = NavHostFragment.findNavController(this);
                navController.popBackStack();

                placeViewModel.setSelected(null);
            } else if (status == AddMatchViewModel.Status.FAILED) {
                Toast.makeText(requireContext(),
                        getResources().getString(R.string.editFailure), Toast.LENGTH_SHORT).show();
            }
            if (matchViewModel.getStatus().getValue() != null)
                matchViewModel.getStatus().setValue(null);
        });

/*        MaterialDatePicker.Builder dpBuilder = MaterialDatePicker.Builder.datePicker();
        dpBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = dpBuilder.build();


        dateEditText.setOnClickListener(v -> {
            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });*/


        dateEditText.setOnTouchListener(((v, motionEvent) -> true));

        selectDateButton.setOnClickListener((v) -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            DatePickerDialog picker = new DatePickerDialog(requireContext(), R.style.DateDialogTheme,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, year, month, day);
            picker.show();
        });


        timeEditText.setOnTouchListener(((v, motionEvent) -> true));

        selectTimeButton.setOnClickListener((v) -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minute = cldr.get(Calendar.MINUTE);
            TimePickerDialog picker;
            picker = new TimePickerDialog(requireContext(), R.style.TimePickerDialogTheme,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            timeEditText.setText(hour + ":" + minute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
            picker.show();
        });


        if (matchViewModel.getMatch().getId() != null) {
            titleEditText.setText(matchViewModel.getMatch().getTitolo());
            sportSpinner.setSelection(
                    Sport.valueOf(matchViewModel.getMatch().getSport().toUpperCase()).ordinal());

            if (placeViewModel.getSelected().getValue() == null) {
                //TODO /!\ Temporaneo
                placeViewModel.setSelected(placeViewModel.getPlaces().getValue().stream()
                        .filter(x -> x.getId().equals(matchViewModel.getMatch().getLuogo()))
                        .findFirst().get());
            }


            dateEditText.setText(matchViewModel.getMatch().getDay()
                    + "/" + (matchViewModel.getMatch().getMonth())
                    + "/" + matchViewModel.getMatch().getYear());
            timeEditText.setText(matchViewModel.getMatch().getHour()
                    + ":" + matchViewModel.getMatch().getMinutes());
            numberOfTeamsEditText.setText(Integer.toString(matchViewModel.getMatch().getNumeroSquadre()));
            costEditText.setText(Double.toString(matchViewModel.getMatch().getCosto()));
            isPrivateSwitch.setChecked(matchViewModel.getMatch().isPrivate());
            descriptionEditText.setText(matchViewModel.getMatch().getDescrizione());
        }

        if (matchViewModel.isCreationModeEnabled()) {
            ((AppCompatActivity) requireActivity()).setTitle(R.string.createGame);
            deleteButton.setVisibility(View.GONE);
        } else {
            ((AppCompatActivity) requireActivity()).setTitle(R.string.match + " "
                    + matchViewModel.getMatch().getTitolo());
        }

        if (matchViewModel.isCreatorUser()) {
            joinButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.GONE);
        } else {
            titleEditText.setEnabled(false);
            sportSpinner.setEnabled(false);
            locationEditText.setEnabled(false);
            dateEditText.setEnabled(false);
            timeEditText.setEnabled(false);
            numberOfTeamsEditText.setEnabled(false);
            costEditText.setEnabled(false);
            isPrivateSwitch.setEnabled(false);
            descriptionEditText.setEnabled(false);
            selectLocationButton.setVisibility(View.GONE);
            selectDateButton.setVisibility(View.GONE);
            selectDateButton.setVisibility(View.GONE);
            selectTimeButton.setVisibility(View.GONE);

            saveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            if (matchViewModel.isParticipantUser()) {
                joinButton.setVisibility(View.GONE);
            } else {
                leaveButton.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }


    static class SpinnerItem {

        public final String key;
        public final String value;

        private SpinnerItem(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static MatchFragment.SpinnerItem create(String key, String value) {
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