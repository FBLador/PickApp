package it.unimib.pickapp.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Place;
import it.unimib.pickapp.ui.placeholder.PlaceholderContent.PlaceholderItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CustomAdapter";

    private ArrayList<Place> places;

    public PlaceRecyclerViewAdapter() {
        this.places = new ArrayList<Place>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.place_fragment_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        holder.getNameTextView().setText(places.get(position).getName());
        holder.getAddressTextView().setText(places.get(position).getAddress());
    }

    public void updatePlaceList(ArrayList<Place> places) {
        this.places.clear();
        this.places = places;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView addressTextView;
        private final FrameLayout container;

        public ViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.nameTextView);
            addressTextView = v.findViewById(R.id.addressTextView);
            container = v.findViewById(R.id.container);
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public FrameLayout getContainer() {
            return container;
        }

        public TextView getAddressTextView() {
            return addressTextView;
        }
    }
}