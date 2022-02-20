//package it.unimib.pickapp.ui;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.navigation.NavController;
//import androidx.navigation.fragment.NavHostFragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import it.unimib.pickapp.R;
//
//public class ParticipantListFragment extends Fragment {
//
//    UserAdapter adapter; // Create Object of the Adapter class
//    private ParticipantListViewModel viewModel;
//
//    public static ParticipantListFragment newInstance() {
//        return new ParticipantListFragment();
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.participant_list_fragment, container, false);
//
//        viewModel = new ViewModelProvider(requireActivity()).get(ParticipantListViewModel.class);
//
//        RecyclerView recyclerView = rootView.findViewById(R.id.recycler);
//
//        // To display the Recycler view linearly
//        recyclerView.setLayoutManager(
//                new LinearLayoutManager(requireContext()));
//
//        adapter = new PlaceAdapter(options, itemClickListener);
//        // Connecting Adapter class with the Recycler view*/
//        recyclerView.setAdapter(adapter);
//
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        // TODO: Use the ViewModel
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }
//}