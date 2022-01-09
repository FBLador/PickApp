package it.unimib.pickapp.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import it.unimib.pickapp.model.Place;

public class PlaceViewModel extends ViewModel {
    private final MutableLiveData<Place> selected;
    private MutableLiveData<ArrayList<Place>> places;

    public PlaceViewModel() {
        selected = new MutableLiveData<Place>(null);
    }

    public LiveData<ArrayList<Place>> getPlaces() {
        if (places == null) {
            places = new MutableLiveData<ArrayList<Place>>();
            loadPlaces();
        }
        return places;
    }

    public LiveData<Place> getSelected() {
        return selected;
    }

    public void setSelected(Place selected) {
        this.selected.setValue(selected);
    }

    private void loadPlaces() {
        ArrayList<Place> newPlaces = new ArrayList<>();
        newPlaces.add(new Place("location1", "Parco Bianco", "Piazza del Parco B.", null));
        newPlaces.add(new Place("location2", "Parco Rosso", "Viale del Parco R", null));
        newPlaces.add(new Place("location3", "Parco Verde", "Via del Parco V.", null));
        newPlaces.add(new Place("location4", "Parco Nero", "Piazza del Parco N.", null));
        places.setValue(newPlaces);
    }
}
