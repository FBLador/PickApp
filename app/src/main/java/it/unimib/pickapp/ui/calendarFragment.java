package it.unimib.pickapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;

public class calendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerViewGames;
    private FloatingActionButton addButton;
    private RecyclerView.LayoutParams params;

    matchesAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the Firebase Realtime Database

    public calendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        calendarView = view.findViewById(R.id.calendarView);
        addButton = view.findViewById(R.id.create_game);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull final CalendarView view, final int year, final int month,
                                            final int dayOfMonth) {
                Toast.makeText(getActivity(), "Date changed to" + dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();

                // Create a instance of the database and get
                // its reference
                mbase = FirebaseDatabase.getInstance().getReference();

                recyclerViewGames = view.findViewById(R.id.recycler_view_games);

                // To display the Recycler view linearly
                recyclerViewGames.setLayoutManager(
                        new LinearLayoutManager(calendarFragment.super.getContext()));

                // It is a class provide by the FirebaseUI to make a
                // query in the database to fetch appropriate data
                FirebaseRecyclerOptions<Match> options
                        = new FirebaseRecyclerOptions.Builder<Match>()
                        .setQuery(mbase, Match.class)
                        .build();
                // Connecting object of required Adapter class to
                // the Adapter class itself
                adapter = new matchesAdapter(options);
                // Connecting Adapter class with the Recycler view*/
                recyclerViewGames.setAdapter(adapter);
            }

            // Function to tell the app to start getting
            // data from database on starting of the activity
            protected void onStart()
            {
                adapter.startListening();
            }

            // Function to tell the app to stop getting
            // data from database on stopping of the activity
            protected void onStop()
            {
                adapter.stopListening();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*
            da inserire aggiunta evento
            openAddEvent();
             */
            }
        });

    }
}