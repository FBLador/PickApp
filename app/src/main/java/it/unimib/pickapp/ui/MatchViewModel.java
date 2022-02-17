package it.unimib.pickapp.ui;

import static it.unimib.pickapp.repository.Constants.FIREBASE_DATABASE_URL;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import it.unimib.pickapp.model.Match;

public class MatchViewModel extends ViewModel {

    private static final String TAG = "MatchViewModel";
    private final DatabaseReference databaseReference;
    private final String collectionName = "Matches";
    private final MutableLiveData<AddMatchViewModel.Status> status;
    private final String currentUserId;
    private Match match;
    private boolean creationModeEnabled;
    private boolean creatorUser;
    private boolean participantUser;


    public MatchViewModel() {
        status = new MutableLiveData<AddMatchViewModel.Status>(null);

        this.match = new Match();

        databaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).
                getReference().child(this.collectionName);

        currentUserId = Objects.requireNonNull(
                FirebaseAuth.getInstance().getCurrentUser()).getUid();
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

        participantUser = match.getParticipants().containsKey(currentUserId);
    }

    public MutableLiveData<AddMatchViewModel.Status> getStatus() {
        return status;
    }

    public void joinMatch() {
        match.getParticipants().put(currentUserId, true);
        saveMatch();
    }

    public void leaveMatch() {
        match.getParticipants().remove(currentUserId);
        saveMatch();
    }

    public void saveMatch() {
        final String id;
        match.setMonth(match.getMonth());
        if (match.getId() == null) {
            id = databaseReference.push().getKey();
            match.setId(id);
            match.setCreatorId(currentUserId);
            match.getParticipants().put(match.getCreatorId(), true);
        } else {
            id = match.getId();
        }
        assert id != null;
        DatabaseReference reference = databaseReference.child(id);
        Log.i(TAG, "Saving '" + id + "' in '" + collectionName + "'.");
        reference.setValue(match).addOnFailureListener(e ->
                Log.d(TAG, "There was an error saving '"
                        + id + "' in '" + collectionName + "'!", e))
                .addOnSuccessListener(t -> status.setValue(AddMatchViewModel.Status.SUCCESSFUL))
                .addOnFailureListener(t -> status.setValue(AddMatchViewModel.Status.FAILED));
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

    public enum Status {
        FAILED,
        SUCCESSFUL
    }

}