package it.unimib.pickapp.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.pickapp.model.Place;

public class FPlaceSelectionViewModel extends ViewModel {
    private final MutableLiveData<Place> selected;

    public FPlaceSelectionViewModel() {
        selected = new MutableLiveData<Place>(null);
    }

    public LiveData<Place> getSelected() {
        return selected;
    }

    public void clearSelection() {
        selected.setValue(null);
    }

    public void setSelected(Place selected) {
        this.selected.setValue(selected);
    }
}