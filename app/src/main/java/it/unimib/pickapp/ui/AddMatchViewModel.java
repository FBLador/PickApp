package it.unimib.pickapp.ui;

import static it.unimib.pickapp.repository.Constants.FIREBASE_DATABASE_URL;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.pickapp.model.Match;

public class AddMatchViewModel extends ViewModel {
    private static final String TAG = "AddMatchViewModel";

    private final Match match;

    private final DatabaseReference databaseReference;
    private final String collectionName = "Matches";

    MutableLiveData<Status> status;

    public AddMatchViewModel() {
        status = new MutableLiveData<Status>(null);

        this.match = new Match();

        databaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).
                getReference().child(this.collectionName);
    }

    public Match getMatch() {
        return match;
    }

    public void saveMatch() {
        final String id;
        if (match.getId() == null) {
            id = databaseReference.push().getKey();
        } else {
            id = match.getId();
        }
        assert id != null;
        DatabaseReference reference = databaseReference.child(id);
        Log.i(TAG, "Creating '" + id + "' in '" + collectionName + "'.");
        reference.setValue(match).addOnFailureListener(e ->
                Log.d(TAG, "There was an error creating '"
                        + id + "' in '" + collectionName + "'!", e))
                .addOnSuccessListener(t -> status.setValue(Status.SUCCESSFUL))
                .addOnFailureListener(t -> status.setValue(Status.FAILED));
    }

    public enum Status {
        FAILED,
        SUCCESSFUL
    }
}
