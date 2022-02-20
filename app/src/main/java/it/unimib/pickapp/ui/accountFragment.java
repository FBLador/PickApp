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
import android.widget.ImageButton;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;

/**
 * It shows the account section.
 */
public class accountFragment extends Fragment {

    private Toolbar toolbar;
    private static final String TAG = "AccountFragment";
    private static final String SHARED_PREF_EMAIL = "email";
    private ImageView imageProfile;
    private TextView matches, fullname, bio, nickname;
    private Button editProfile;

    //get info from firebase
    private FirebaseUser user;
    private DatabaseReference reference, locationReference;
    private String userID;

    private RecyclerView recyclerView;
    matchesAdapter adapter;

    private ImageButton matches_profile, posts;


    public accountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // It is necessary to specify that the toolbar has a custom menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);// Inflate the layout for this fragment
        Toolbar toolbar = view.findViewById(R.id.toolbarAccount);
        Objects.requireNonNull(((pickappActivity) requireActivity()).getSupportActionBar()).hide();
        ((pickappActivity) getActivity()).setSupportActionBar(toolbar);

        imageProfile = view.findViewById(R.id.image_profile);
        matches = view.findViewById(R.id.matches);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        editProfile = view.findViewById(R.id.edit_profile);
        nickname = view.findViewById(R.id.nickname);

        recyclerView = view.findViewById(R.id.recycler_view_matches_profile);

        //to display the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        creaRecyclerView();

        getNrMatches();

        userInfo(view);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfileActivity();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // The custom menu that we want to add to the toolbar
        inflater.inflate(R.menu.logout_menu, menu);
    }

    private void userInfo(View view) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        Log.d(TAG, userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nick_name = snapshot.child(userID).child("nickname").getValue(String.class);
                String full_name = snapshot.child(userID).child("fullname").getValue(String.class);
                String b_io = snapshot.child(userID).child("bio").getValue(String.class);
                String image_profile = snapshot.child(userID).child("imageurl").getValue(String.class);

                nickname.setText(nick_name);
                fullname.setText(full_name);
                bio.setText(b_io);
                Glide.with(view)
                        .load(image_profile)
                        .circleCrop()
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .into(imageProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNrMatches(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Matches");
        locationReference = FirebaseDatabase.getInstance().getReference("Places");
        Query query = reference.orderByChild("participants/" + userID).startAt("\u0000").endAt("\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        i++;
                }
                matches.setText(String.valueOf(i));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    private void openEditProfileActivity() {
        Intent intent = new Intent(getActivity(), editProfileActivity.class);
        startActivity(intent);
    }

    public void creaRecyclerView() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Matches");
        locationReference = FirebaseDatabase.getInstance().getReference("Places");
        Query query = reference.orderByChild("participants/" + userID).startAt("\u0000").endAt("\uf8ff");
        FirebaseRecyclerOptions<Match> options
                = new FirebaseRecyclerOptions.Builder<Match>()
                .setQuery(query, Match.class)
                .build();


        matchesAdapter.ItemClickListener itemClickListener = match -> {
            MatchViewModel matchViewModel =
                    new ViewModelProvider(requireActivity()).get(MatchViewModel.class);

            matchViewModel.setMatch(match);
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.action_account_to_match);
        };
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new matchesAdapter(options, itemClickListener, locationReference, false);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        recyclerView.invalidate();
    }


}