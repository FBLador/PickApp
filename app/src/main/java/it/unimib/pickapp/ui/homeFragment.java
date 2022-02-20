package it.unimib.pickapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;

/**
 * It shows the homepage of the app.
 */
public class homeFragment extends Fragment {

    private ImageButton basket, soccer, tennis, football;
    private RecyclerView recyclerView;
    private DatabaseReference mbase;
    private DatabaseReference locationReference;

    private static final String TAG = "homeFragment";
    matchesAdapter adapter; // Create Object of the Adapter class
    private String filtroSport;
    //get info from firebase
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;


    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // It is necessary to specify that the toolbar has a custom menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbarHome);
        Objects.requireNonNull(((pickappActivity) requireActivity()).getSupportActionBar()).hide();
        ((pickappActivity) getActivity()).setSupportActionBar(toolbar);

        setTitle(view, getString(R.string.home));
        basket = view.findViewById(R.id.basketFilter);
        tennis = view.findViewById(R.id.tennisFilter);
        soccer = view.findViewById(R.id.soccerFilter);
        football = view.findViewById(R.id.footballFilter);
        recyclerView = view.findViewById(R.id.recycler_view_games);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        return view;
    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filtroSport = snapshot.child(userID).child("favouriteSport").getValue().toString().toUpperCase(Locale.ROOT);
                switch (filtroSport) {
                    case "BASKETBALL":
                        activateImgBttn(basket);
                        break;
                    case "SOCCER":
                        activateImgBttn(soccer);
                        break;
                    case "FOOTBALL":
                        activateImgBttn(football);
                        break;
                    case "TENNIS":
                        activateImgBttn(tennis);
                        break;
                }
                creaRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        basket.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    filtroSport = "BASKETBALL";
                    creaRecyclerView();
                    activateImgBttn(basket);
                    inactivateImgBttn(soccer);
                    inactivateImgBttn(tennis);
                    inactivateImgBttn(football);
                }
                return true;
            }
        });
        soccer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    filtroSport = "SOCCER";
                    creaRecyclerView();
                    activateImgBttn(soccer);
                    inactivateImgBttn(football);
                    inactivateImgBttn(tennis);
                    inactivateImgBttn(basket);
                }
                return true;
            }
        });
        tennis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    filtroSport = "TENNIS";
                    creaRecyclerView();
                    activateImgBttn(tennis);
                    inactivateImgBttn(soccer);
                    inactivateImgBttn(football);
                    inactivateImgBttn(basket);
                }
                return true;
            }
        });
        football.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    filtroSport = "FOOTBALL";
                    creaRecyclerView();
                    activateImgBttn(football);
                    inactivateImgBttn(soccer);
                    inactivateImgBttn(tennis);
                    inactivateImgBttn(basket);
                }
                return true;
            }
        });
    }

    public void setTitle(View view, String title){
        TextView titleToolbar = view.findViewById(R.id.titleHome);
        titleToolbar.setText(title);
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
    public void activateImgBttn(ImageButton bttn) {
        bttn.setScaleX(1.2F);
        bttn.setScaleY(1.2F);
        bttn.setColorFilter(ContextCompat.getColor(getContext(), R.color.mainGreen));
    }
    public void inactivateImgBttn(ImageButton bttn) {
        bttn.setScaleY(1);
        bttn.setScaleX(1);
        bttn.setColorFilter(ContextCompat.getColor(getContext(), R.color.item_color_inactive));
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    public void creaRecyclerView() {
        mbase = FirebaseDatabase.getInstance().getReference("Matches");
        locationReference = FirebaseDatabase.getInstance().getReference("Places");

        Query query = mbase.orderByChild("sport").equalTo(filtroSport);

        // TODO
        //.orderByChild("dateTime").equalTo(dayOfMonth + "/" + month + "/" + year)
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
        adapter = new matchesAdapter(options, itemClickListener, locationReference, true);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        recyclerView.invalidate();
    }
}