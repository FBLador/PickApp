package it.unimib.pickapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Place;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class PlaceAdapter extends FirebaseRecyclerAdapter<
        Place, PlaceAdapter.ViewHolder> {

    private final ItemClickListener itemClickListener;

    public PlaceAdapter(@NonNull FirebaseRecyclerOptions<Place> options,
                        ItemClickListener itemClickListener) {
        super(options);
        this.itemClickListener = itemClickListener;
    }

    // Function to bind the view in Card view(here
    // "person.xml") with data in
    // model class(here "person.class")
    @Override
    protected void
    onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Place model) {
        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(model));
        holder.getNameTextView().setText(model.getName());
        holder.getAddressTextView().setText(model.getAddress());
    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    public interface ItemClickListener {
        void onItemClick(Place place);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    static class ViewHolder extends RecyclerView.ViewHolder {
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