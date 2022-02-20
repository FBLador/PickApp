package it.unimib.pickapp.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.pickapp.model.Place;

public class ParticipantListViewModel extends ViewModel {
    private final MutableLiveData<Place> selected;
    DatabaseReference userReference;

    public ParticipantListViewModel() {
        selected = new MutableLiveData<Place>(null);
        userReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    public LiveData<Place> getSelected() {
        return selected;
    }


}