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

public class FPlaceSelectionFragment extends Fragment {

    PlaceAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase;
    private FPlaceSelectionViewModel mViewModel;
    private RecyclerView recyclerView;

    public static FPlaceSelectionFragment newInstance() {
        return new FPlaceSelectionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_place_selection_fragment, container, false);

        mViewModel = new ViewModelProvider(requireActivity()).get(FPlaceSelectionViewModel.class);

        mbase = FirebaseDatabase.getInstance().getReference("Places");

        recyclerView = rootView.findViewById(R.id.placesRecycler);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Place> options
                = new FirebaseRecyclerOptions.Builder<Place>()
                .setQuery(mbase, Place.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        PlaceAdapter.ItemClickListener itemClickListener = place -> {
            mViewModel.setSelected(place);

            NavController navController = NavHostFragment.findNavController(FPlaceSelectionFragment.this);
            navController.popBackStack();
        };
        adapter = new PlaceAdapter(options, itemClickListener);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        /*.addOnItemTouchListener(
                new RecyclerItemClickListener(requireContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mViewModel.setSelected(mViewModel.getPlaces().getValue().get(position));

                        NavController navController = NavHostFragment.findNavController(FPlaceSelectionFragment.this);
                        navController.popBackStack();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );*/

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