package it.unimib.pickapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;

/**
 * It shows the homepage of the app.
 */
public class homeFragment extends Fragment {

    private TextView titleToolbar;
    private RecyclerView recyclerView;
    DatabaseReference mbase;
    private static final String TAG = "homeFragment";
    matchesAdapter adapter; // Create Object of the Adapter class

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        setTitle(view, getString(R.string.home));
        recyclerView = view.findViewById(R.id.recycler_view_games);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        return view;
    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {

        mbase = FirebaseDatabase.getInstance().getReference("Matches");
        Log.d(TAG, String.valueOf(mbase.orderByChild("sport").equalTo("TENNIS")));

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
    }

    public void setTitle(View view, String title){
        titleToolbar = view.findViewById(R.id.titleHome);
        titleToolbar.setText(title);
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