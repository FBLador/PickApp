package it.unimib.pickapp.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.pickapp.model.Match;
import it.unimib.pickapp.model.User;

public class ParticipantListViewModel extends ViewModel {
    private final DatabaseReference userReference;
    private Match match;
    private List<User> users;
    private UserAdapter adapter;

    public ParticipantListViewModel() {
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        users = new ArrayList<>();
    }

    public void loadParticipants() {
        for (String userId : match.getParticipants().keySet()) {
            userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users.add(snapshot.getValue(User.class));
                    adapter.notifyItemChanged(users.size() - 1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setAdapter(UserAdapter adapter) {
        this.adapter = adapter;
    }
}