package it.unimib.pickapp.ui;

import static it.unimib.pickapp.Constants.FIREBASE_DATABASE_URL;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import it.unimib.pickapp.model.Match;
import it.unimib.pickapp.model.Place;
import it.unimib.pickapp.model.User;

public class MatchViewModel extends ViewModel {

    private static final String TAG = "MatchViewModel";
    private final DatabaseReference matchDatabaseReference;
    private final DatabaseReference locationDatabaseReference;
    private final String collectionName = "Matches";
    private final MutableLiveData<MatchViewModel.Status> status;
    private final String currentUserId;
    private Match match;
    private boolean creationModeEnabled;
    private boolean creatorUser;
    private boolean participantUser;
    private final MutableLiveData<Place> selectedPlace;
    private ArrayList<User> participants;
    private final DatabaseReference databaseReferencetoUser;



    public MatchViewModel() {
        status = new MutableLiveData<>(null);

        this.match = new Match();

        matchDatabaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).
                getReference().child(this.collectionName);

        databaseReferencetoUser = FirebaseDatabase.getInstance().getReference("Users");

        locationDatabaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).
                getReference().child("Places");

        currentUserId = Objects.requireNonNull(
                FirebaseAuth.getInstance().getCurrentUser()).getUid();
        selectedPlace = new MutableLiveData<>(null);
        creationModeEnabled = true;
        creatorUser = false;
        participantUser = false;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;

        creationModeEnabled = match.getId() == null;

        creatorUser = creationModeEnabled || match.getCreatorId().equals(
                Objects.requireNonNull(
                        FirebaseAuth.getInstance().getCurrentUser()).getUid());

        if (match.getLuogo() != null) {
            locationDatabaseReference.child(match.getLuogo()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            selectedPlace.setValue(dataSnapshot.getValue(Place.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    }
            );
        }

        participantUser = match.getParticipants().containsKey(currentUserId);

        participants = new ArrayList<User>();

        for (String userID: match.getParticipants().keySet()) {
            //.child(userID);
            databaseReferencetoUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //TODO Participants
                    //participants.add(dataSnapshot.child(userID).getValue(User.class));
                    System.out.println(participants);
                    Log.d(TAG, "user" + participants);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    public MutableLiveData<MatchViewModel.Status> getStatus() {
        return status;
    }

    public void joinMatch() {
        match.getParticipants().put(currentUserId, match.getDate());
        saveMatch();
    }

    public void leaveMatch() {
        match.getParticipants().remove(currentUserId);
        saveMatch();
    }

    public void saveMatch() {
        final String id;

        if (match.getId() == null) {
            id = matchDatabaseReference.push().getKey();
            match.setId(id);
            match.setCreatorId(currentUserId);
            match.getParticipants().put(match.getCreatorId(), match.getDate());
        } else {
            id = match.getId();
        }
        assert id != null;
        DatabaseReference reference = matchDatabaseReference.child(id);
        Log.i(TAG, "Saving '" + id + "' in '" + collectionName + "'.");
        reference.setValue(match).addOnFailureListener(e ->
                Log.d(TAG, "There was an error saving '"
                        + id + "' in '" + collectionName + "'!", e))
                .addOnSuccessListener(t -> status.setValue(MatchViewModel.Status.SUCCESSFUL))
                .addOnFailureListener(t -> status.setValue(MatchViewModel.Status.FAILED));
    }

    public boolean isCreationModeEnabled() {
        return creationModeEnabled;
    }

    public boolean isCreatorUser() {
        return creatorUser;
    }

    public boolean isParticipantUser() {
        return participantUser;
    }

    public LiveData<Place> getSelectedPlace() {
        return selectedPlace;
    }

    public void setSelectedPlace(Place place) {
        selectedPlace.setValue(place);
    }

    public void clearPlaceSelection() {
        selectedPlace.setValue(null);
    }

    public enum Status {
        FAILED,
        SUCCESSFUL
    }

}