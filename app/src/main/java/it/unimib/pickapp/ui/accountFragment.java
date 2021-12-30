package it.unimib.pickapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import it.unimib.pickapp.R;

/**
 * It shows the account section.
 */
public class accountFragment extends Fragment {

    private Toolbar toolbar;

    private static final String TAG = "AccountFragment";
    private static final String SHARED_PREF_EMAIL = "email";
    private ImageView imageProfile;
    private TextView match, won, played, fullname, bio, nickname;
    private Button editProfile;


    //get info from firebase
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private RecyclerView recyclerView_activities;

    private RecyclerView recyclerView_post;

    private ImageButton activities, posts;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);// Inflate the layout for this fragment
        Toolbar toolbar = view.findViewById(R.id.toolbarAccount);
        Objects.requireNonNull(((pickappActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
        ((pickappActivity) getActivity()).setSupportActionBar(toolbar);

        imageProfile = view.findViewById(R.id.image_profile);
        match = view.findViewById(R.id.matches);
        won = view.findViewById(R.id.won);
        played = view.findViewById(R.id.played);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        editProfile = view.findViewById(R.id.edit_profile);
        nickname = view.findViewById(R.id.nickname);
        activities = view.findViewById(R.id.activities);
        posts = view.findViewById(R.id.posts);

        recyclerView_activities = view.findViewById(R.id.recycler_view_activities);
        recyclerView_activities.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView_activities.setLayoutManager(mLayoutManager);

        recyclerView_post = view.findViewById(R.id.recycler_view_posts);
        recyclerView_post.setHasFixedSize(true);
        LinearLayoutManager mLayoutManagers = new GridLayoutManager(getContext(), 3);
        recyclerView_post.setLayoutManager(mLayoutManagers);

        recyclerView_activities.setVisibility(View.VISIBLE);
        recyclerView_post.setVisibility(View.GONE);

        userInfo(view);

        activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView_activities.setVisibility(View.VISIBLE);
                recyclerView_post.setVisibility(View.GONE);
            }
        });

        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView_activities.setVisibility(View.GONE);
                recyclerView_post.setVisibility(View.VISIBLE);
            }
        });

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

               // Log.d(TAG, nick_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
/*
    private void getNrMatches(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        Log.d(TAG, userID);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Matches");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Match match = snapshot.getValue(Match.class);
                    if (match.contains(userID)){
                        Log.d(TAG, userID);
                        i++;
                    }
                }
                match.setText(""+i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

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


}