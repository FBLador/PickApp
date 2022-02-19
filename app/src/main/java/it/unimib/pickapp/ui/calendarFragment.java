package it.unimib.pickapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;

public class calendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private View HideShowView;
    private FloatingActionButton addButton;
    private Button hideShowButton;
    private DatabaseReference mbase;
    private DatabaseReference locationReference;
    private String selectedDate;
    private Query query;
    boolean flag=true;
    private static final String TAG = "calendarFragment";
    private matchesAdapter adapter; // Create Object of the Adapter class
    private matchesAdapter.ItemClickListener itemClickListener;

    public calendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // It is necessary to specify that the toolbar has a custom menu
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        setTitle(rootView, getString(R.string.calendar));
        Toolbar toolbar = rootView.findViewById(R.id.toolbarCalendar);
        Objects.requireNonNull(((pickappActivity) requireActivity()).getSupportActionBar()).hide();
        ((pickappActivity) getActivity()).setSupportActionBar(toolbar);

        calendarView = rootView.findViewById(R.id.calendarView);
        HideShowView = rootView.findViewById(R.id.linearLayout2);
        addButton = rootView.findViewById(R.id.create_game);
        hideShowButton = rootView.findViewById(R.id.HideShow);

        hideShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.HideShow:
                        if(flag)
                        {
                            HideShowView.setVisibility(View.GONE);
                            flag=false;
                        }
                        else {
                            HideShowView.setVisibility(View.VISIBLE);
                            flag=true;
                        }
                }
            }
        });
        recyclerView = rootView.findViewById(R.id.recycler_view_games);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));


        // Choose time zone in which you want to interpret your Date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        selectedDate = day + "/"
                + (month+1) + "/"
                + year;

        mbase = FirebaseDatabase.getInstance().getReference("Matches");
        query = mbase.orderByChild("date").equalTo(selectedDate);
        locationReference = FirebaseDatabase.getInstance().getReference("Places");

        FirebaseRecyclerOptions<Match> options
                = new FirebaseRecyclerOptions.Builder<Match>()
                .setQuery(query, Match.class)
                .build();

        matchesAdapter.ItemClickListener itemClickListener = match -> {
            MatchViewModel matchViewModel =
                    new ViewModelProvider(requireActivity()).get(MatchViewModel.class);

            matchViewModel.setMatch(match);
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_home_to_match);
        };
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new matchesAdapter(options, itemClickListener, locationReference);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        recyclerView.invalidate();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            //Toast.makeText(getActivity(), "Date changed to " + dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Date changed to " + dayOfMonth + "/" + month + "/" + year);

            selectedDate = dayOfMonth + "/" + (month+1) + "/" + year;
            mbase = FirebaseDatabase.getInstance().getReference("Matches");
            query = mbase.orderByChild("date").equalTo(selectedDate);
            locationReference = FirebaseDatabase.getInstance().getReference("Places");

            FirebaseRecyclerOptions<Match> options
                    = new FirebaseRecyclerOptions.Builder<Match>()
                    .setQuery(query, Match.class)
                    .build();

            matchesAdapter.ItemClickListener itemClickListener = match -> {
                MatchViewModel matchViewModel =
                        new ViewModelProvider(requireActivity()).get(MatchViewModel.class);

                matchViewModel.setMatch(match);
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.action_home_to_match);
            };
            // Connecting object of required Adapter class to
            // the Adapter class itself
            adapter = new matchesAdapter(options, itemClickListener, locationReference);
            // Connecting Adapter class with the Recycler view*/
            recyclerView.setAdapter(adapter);
            adapter.startListening();
            recyclerView.invalidate();
        });

        addButton.setOnClickListener(v -> {
            MatchViewModel matchViewModel =
                    new ViewModelProvider(requireActivity()).get(MatchViewModel.class);

            matchViewModel.setMatch(new Match());
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_calendar_to_match);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void setTitle(View view, String titleCal){
        TextView titleToolbar = view.findViewById(R.id.titleCalendar);
        titleToolbar.setText(titleCal);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // The custom menu that we want to add to the toolbar
        inflater.inflate(R.menu.logout_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Listener for the items in the custom menu
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(requireActivity(), loginActivity.class));
            requireActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}