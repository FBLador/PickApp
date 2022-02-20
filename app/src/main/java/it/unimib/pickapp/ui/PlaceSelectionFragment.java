package it.unimib.pickapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Place;

public class PlaceSelectionFragment extends Fragment {

    PlaceAdapter adapter; // Create Object of the Adapter class
    DatabaseReference placeReference;
    private PlaceSelectionViewModel viewModel;

    public static PlaceSelectionFragment newInstance() {
        return new PlaceSelectionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.place_selection_fragment, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(PlaceSelectionViewModel.class);

        placeReference = FirebaseDatabase.getInstance().getReference("Places");

        RecyclerView recyclerView = rootView.findViewById(R.id.placesRecycler);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Place> options
                = new FirebaseRecyclerOptions.Builder<Place>()
                .setQuery(placeReference, Place.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        PlaceAdapter.ItemClickListener itemClickListener = place -> {
            viewModel.setSelected(place);

            NavController navController = NavHostFragment.findNavController(PlaceSelectionFragment.this);
            navController.popBackStack();
        };
        adapter = new PlaceAdapter(options, itemClickListener);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}