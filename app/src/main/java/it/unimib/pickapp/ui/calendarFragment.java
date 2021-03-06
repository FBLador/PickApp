package it.unimib.pickapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;
import java.util.TimeZone;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;

public class calendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private View HideShowView;
    private FloatingActionButton addButton;
    private ImageButton hideShowButton;
    private DatabaseReference mbase;
    private DatabaseReference locationReference;
    private String selectedDate;
    private Query query;
    boolean flag = true;
    private static final String TAG = "calendarFragment";
    private matchesAdapter adapter; // Create Object of the Adapter class
    private matchesAdapter.ItemClickListener itemClickListener;
    private FirebaseUser user;
    private String userID;
    private String participant;

    public calendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        setTitle(rootView, getString(R.string.calendar));

        calendarView = rootView.findViewById(R.id.calendarView);
        HideShowView = rootView.findViewById(R.id.linearLayout2);
        addButton = rootView.findViewById(R.id.create_game);
        hideShowButton = rootView.findViewById(R.id.HideShow);

        hideShowButton.setOnClickListener(v -> {
            if (v.getId() == R.id.HideShow) {
                if (flag) {
                    HideShowView.setVisibility(View.GONE);
                    inactivateImgButton(hideShowButton);
                    flag = false;
                } else {
                    HideShowView.setVisibility(View.VISIBLE);
                    activateImgButton(hideShowButton);
                    flag = true;
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

        /*selectedDate = day + "-"
                        + (month+1) + "-"
                        + year;*/

        selectedDate = day + "/"
                + (month + 1) + "/"
                + year;

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        participant = userID + "-" + selectedDate;
        mbase = FirebaseDatabase.getInstance().getReference("Matches");
        //query = mbase.orderByChild("participants/" + participant).equalTo(true);
        query = mbase.orderByChild("participants/" + userID).equalTo(selectedDate);
        //query = mbase.orderByChild("participants/" + "OMyc2NKRLlTyTQs7PGJxP2qErvf1").equalTo("20/2/2022");

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
            navController.navigate(R.id.action_calendar_to_match);
        };
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new matchesAdapter(options, itemClickListener, locationReference, false);
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

            //selectedDate = dayOfMonth + "-" + (month+1) + "-" + year;
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            participant = userID + "-" + selectedDate;
            mbase = FirebaseDatabase.getInstance().getReference("Matches");
            //query = mbase.orderByChild("participants/" + participant).equalTo(true);
            query = mbase.orderByChild("participants/" + userID).equalTo(selectedDate);
            Log.i(TAG, selectedDate);
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
                navController.navigate(R.id.action_calendar_to_match);
            };
            // Connecting object of required Adapter class to
            // the Adapter class itself
            adapter = new matchesAdapter(options, itemClickListener, locationReference, false);
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


    public void activateImgButton(ImageButton button) {
        button.setScaleX(1.2F);
        button.setScaleY(1.2F);
        button.setColorFilter(ContextCompat.getColor(getContext(), R.color.mainGreen));
    }


    public void inactivateImgButton(ImageButton button) {
        button.setScaleY(1);
        button.setScaleX(1);
        button.setColorFilter(ContextCompat.getColor(getContext(), R.color.item_color_inactive));
    }
}