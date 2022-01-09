package it.unimib.pickapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import it.unimib.pickapp.R;
import it.unimib.pickapp.utils.RecyclerItemClickListener;

/**
 * A fragment representing a list of Items.
 */
public class PlaceFragment extends Fragment {

    private static final String TAG = "PlaceFragment";
    private static final int DATASET_COUNT = 60;

    private static PlaceViewModel mViewModel;
    protected PlaceRecyclerViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;

    public static PlaceFragment newInstance() {
        return new PlaceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(PlaceViewModel.class);
        mViewModel.getPlaces();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.place_fragment_item_list, container, false);
        rootView.setTag(TAG);

        mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.placesRecycler);

        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PlaceRecyclerViewAdapter();
        recyclerView.setAdapter(mAdapter);

        mViewModel.getPlaces().observe(this, (places -> mAdapter.updatePlaceList(places)));


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(requireContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        mViewModel.setSelected(mViewModel.getPlaces().getValue().get(position));

                        FragmentManager manager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction ft = manager.beginTransaction();
                        // TODO ft.setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
                        ft.remove(Objects.requireNonNull(manager.findFragmentByTag("PlaceFragment")));
                        ft.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        return rootView;
    }

}