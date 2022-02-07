package it.unimib.pickapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;

public class calendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private RecyclerView.LayoutParams params;
    DatabaseReference mbase;

    //get info from firebase
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private static final String TAG = "calendarFragment";
    private TextView titolo, luogo, numeroSquadre, data, durata, costo, sport, descrizione;

    matchesAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mDatabase; // Create object of the Firebase Realtime Database

    public calendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = rootView.findViewById(R.id.calendarView);
        addButton = rootView.findViewById(R.id.create_game);

        recyclerView = rootView.findViewById(R.id.recycler_view_games);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        // Create a instance of the database and get
        // its reference
        mbase = FirebaseDatabase.getInstance().getReference("Matches");


        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Match> options
                = new FirebaseRecyclerOptions.Builder<Match>()
                .setQuery(mbase, Match.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new matchesAdapter(options);
        // Connecting Adapter class with the Recycler view
        recyclerView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {



        /*
        List<Match> matchList = new ArrayList<>();
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                matchList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Match match = postSnapshot.getValue(Match.class);
                    matchList.add(match);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });


        System.out.println(matchList); */

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull final CalendarView view, final int year, final int month,
                                            final int dayOfMonth) {
                Toast.makeText(getActivity(), "Date changed to " + dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Date changed to " + dayOfMonth + "/" + month + "/" + year);

                mbase = FirebaseDatabase.getInstance().getReference("Matches");

                // TODO
                //.orderByChild("dateTime").equalTo(dayOfMonth + "/" + month + "/" + year)
                FirebaseRecyclerOptions<Match> options
                        = new FirebaseRecyclerOptions.Builder<Match>()
                        .setQuery(mbase, Match.class)
                        .build();
                // Connecting object of required Adapter class to
                // the Adapter class itself
                adapter = new matchesAdapter(options);
                // Connecting Adapter class with the Recycler view*/
                recyclerView.setAdapter(adapter);
                adapter.startListening();
                recyclerView.invalidate();

               /*
                // Create a instance of the database and get
                // its reference
                mbase = FirebaseDatabase.getInstance().getReference("Matches");

                recyclerView = getActivity().findViewById(R.id.recycler_view_games);

                // To display the Recycler view linearly
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(getContext()));


                // It is a class provide by the FirebaseUI to make a
                // query in the database to fetch appropriate data
                FirebaseRecyclerOptions<Match> options
                        = new FirebaseRecyclerOptions.Builder<Match>()
                        .setQuery(mbase, Match.class)
                        .build();
                // Connecting object of required Adapter class to
                // the Adapter class itself
                adapter = new matchesAdapter(options);

                // Connecting Adapter class with the Recycler view
                recyclerView.setAdapter(adapter);*/

            }

            //updateRecycler();
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addMatchActivity.class);
                startActivity(intent);
            }
        });



    }

    public void updateRecycler() { //Aggiorna la recycle view con le partite del giorno selezionato
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Matches");
        userID = user.getUid();
        Log.d(TAG, userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child : snapshot.getChildren()) {
                    String titoloPartita = child.child("titolo").getValue(String.class);

                    titolo.setText(titoloPartita);
                }


                // Log.d(TAG, nick_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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

}