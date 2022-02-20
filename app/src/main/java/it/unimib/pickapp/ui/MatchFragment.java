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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

    private PlaceSelectionViewModel placeSelectionViewModel;
    private MatchViewModel matchViewModel;

    public static MatchFragment newInstance() {
        return new MatchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_match, container, false);
    }


    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
        placeSelectionViewModel = new ViewModelProvider(requireActivity()).get(PlaceSelectionViewModel.class);

        Toolbar toolbar = view.findViewById(R.id.toolbarMatch);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        TextView toolbarTitle = view.findViewById(R.id.titleMatch);
        Spinner sportSpinner = view.findViewById(R.id.sportSpinner);
        String[] sportKeys = Sport.names();
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

        List<SpinnerItem> items = new ArrayList<>();

        for (int i = 0; i < sportKeys.length; i++) {
            items.add(SpinnerItem.create(sportKeys[i], values[i]));
        }

        sportSpinner.setAdapter(new ArrayAdapter<>(
                requireContext().getApplicationContext(),
                R.layout.sport_spinner_item, items));

        selectLocationButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_matchFragment_to_FPlaceSelectionFragment);
        });

        locationEditText.setOnTouchListener(((View v, MotionEvent motionEvent) -> true));

        placeSelectionViewModel.getSelected().observe(getViewLifecycleOwner(), selected -> {
            if (selected != null)
                matchViewModel.setSelectedPlace(selected);
        });


        saveButton.setOnClickListener(v -> {
            if (placeSelectionViewModel.getSelected().getValue() == null) {
                String error = getResources().getString(R.string.choosePlaceMessage);
                Toast.makeText(requireContext(),
                        error, Toast.LENGTH_SHORT).show();
                locationEditText.setError(error);
                locationEditText.requestFocus();
                return;
            }

            String[] splitDate = dateEditText.getText().toString().split(Pattern.quote("/"));
            String[] splitTime = timeEditText.getText().toString().split(Pattern.quote(":"));
            if (splitTime.length != 2) {
                String error = getResources().getString(R.string.invalidTimeDate);
                Toast.makeText(requireContext(),
                        error, Toast.LENGTH_SHORT).show();
                timeEditText.setError(error);
                timeEditText.requestFocus();
                return;
            }
            if (splitDate.length != 3) {
                String error = getResources().getString(R.string.invalidTimeDate);
                Toast.makeText(requireContext(),
                        error, Toast.LENGTH_SHORT).show();
                dateEditText.setError(error);
                dateEditText.requestFocus();
                return;
            }
            Match match = matchViewModel.getMatch();
            String title = titleEditText.getText().toString();
            if (title.isEmpty()) {
                String error = getResources().getString(R.string.invalidTitle);
                Toast.makeText(requireContext(),
                        error, Toast.LENGTH_SHORT).show();
                titleEditText.setError(error);
                titleEditText.requestFocus();
                return;
            }
            match.setTitolo(title);
            match.setSport(((SpinnerItem) sportSpinner.getSelectedItem()).getKey());
            match.setLuogo(placeSelectionViewModel.getSelected().getValue().getId());
            match.setDate(dateEditText.getText().toString());
            match.setTime(timeEditText.getText().toString());
            String costString = costEditText.getText().toString();
            double cost;
            if (costString.isEmpty() || (cost = Double.parseDouble(costString)) < 0) {
                String error = getResources().getString(R.string.invalidCost);
                Toast.makeText(requireContext(),
                        error, Toast.LENGTH_SHORT).show();
                costEditText.setError(error);
                costEditText.requestFocus();
                return;
            }
            match.setCosto(cost);
            String numOfTeamsString = numberOfTeamsEditText.getText().toString();
            int numOfTeams;
            if (numOfTeamsString.isEmpty()
                    || (numOfTeams = Integer.parseInt(numOfTeamsString)) <= 0) {
                String error = getResources().getString(R.string.invalidNumOfTeams);
                Toast.makeText(requireContext(),
                        error, Toast.LENGTH_SHORT).show();
                numberOfTeamsEditText.setError(error);
                numberOfTeamsEditText.requestFocus();
                return;
            }
            match.setNumeroSquadre(numOfTeams);
            match.setDescrizione(descriptionEditText.getText().toString());
            match.setPrivate(isPrivateSwitch.isChecked());
            matchViewModel.saveMatch();
        });

        joinButton.setOnClickListener(v -> matchViewModel.joinMatch());

        leaveButton.setOnClickListener(v -> matchViewModel.leaveMatch());

        deleteButton.setOnClickListener(v -> matchViewModel.deleteMatch());

        matchViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status == MatchViewModel.Status.SUCCESSFUL) {
                Toast.makeText(requireContext(),
                        getResources().getString(R.string.editSuccess), Toast.LENGTH_SHORT).show();
                NavController navController = NavHostFragment.findNavController(this);
                navController.popBackStack();
            } else if (status == MatchViewModel.Status.FAILED) {
                Toast.makeText(requireContext(),
                        getResources().getString(R.string.editFailure), Toast.LENGTH_SHORT).show();
            }
            if (matchViewModel.getStatus().getValue() != null)
                matchViewModel.getStatus().setValue(null);
        });

        dateEditText.setOnTouchListener(((v, motionEvent) -> true));

        selectDateButton.setOnClickListener((v) -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            DatePickerDialog picker = new DatePickerDialog(requireContext(), R.style.DateDialogTheme,
                    (view1, year1, monthOfYear, dayOfMonth) ->
                            dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1),
                    year, month, day);
            picker.show();
        });


        timeEditText.setOnTouchListener(((v, motionEvent) -> true));

        selectTimeButton.setOnClickListener((v) -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minute = cldr.get(Calendar.MINUTE);
            TimePickerDialog picker;
            picker = new TimePickerDialog(requireContext(), R.style.TimePickerDialogTheme,
                    (timePicker, selectedHour, selectedMinute) ->
                            timeEditText.setText(selectedHour + ":" + selectedMinute),
                    hour, minute, true);
            picker.show();
        });


        matchViewModel.getSelectedPlace().observe(getViewLifecycleOwner(), selected -> {
            if (selected != null)
                locationEditText.setText(selected.getName());
        });

        if (matchViewModel.getMatch().getId() != null) {
            titleEditText.setText(matchViewModel.getMatch().getTitolo());
            sportSpinner.setSelection(
                    Sport.valueOf(matchViewModel.getMatch().getSport().toUpperCase()).ordinal());

            dateEditText.setText(matchViewModel.getMatch().getDate());
            timeEditText.setText(matchViewModel.getMatch().getTime());
            numberOfTeamsEditText.setText(Integer.toString(matchViewModel.getMatch().getNumeroSquadre()));
            costEditText.setText(Double.toString(matchViewModel.getMatch().getCosto()));
            isPrivateSwitch.setChecked(matchViewModel.getMatch().isPrivate());
            descriptionEditText.setText(matchViewModel.getMatch().getDescrizione());
        }

        if (matchViewModel.isCreationModeEnabled()) {
            toolbarTitle.setText(R.string.createGame);
            deleteButton.setVisibility(View.GONE);
        } else {
            toolbarTitle.setText(matchViewModel.getMatch().getTitolo());
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        matchViewModel.clearPlaceSelection();
        placeSelectionViewModel.clearSelection();
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

        @NonNull
        @Override
        public String toString() {
            return value;
        }

    }

}