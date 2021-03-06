package it.unimib.pickapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;
import it.unimib.pickapp.model.Place;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class matchesAdapter extends FirebaseRecyclerAdapter<Match, matchesAdapter.matchesViewHolder> {
    private final ItemClickListener itemClickListener;
    DatabaseReference locationReference = FirebaseDatabase.getInstance().getReference("Matches");
    boolean hidePrivate;

    public matchesAdapter(
            @NonNull FirebaseRecyclerOptions<Match> options,
            ItemClickListener itemClickListener,
            DatabaseReference locationReference, boolean hidePrivate) {
        super(options);
        this.hidePrivate = hidePrivate;
        this.itemClickListener = itemClickListener;
        this.locationReference = locationReference;
    }

    // Function to bind the view in Card view with data in
    // model class
    @Override
    protected void
    onBindViewHolder(@NonNull matchesViewHolder holder,
                     int position, @NonNull Match model) {


        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(model));

        holder.titolo.setText(model.getTitolo());

        holder.luogo.setText(model.getLuogo());

        //holder.partecipanti.setText(model.getPartecipanti());

        //holder.numeroSquadre.setText(Integer.toString(model.getNumeroSquadre()));

        holder.dateTime.setText(model.getDate() + " " + model.getTime());

        //holder.sport.setText(model.getSport());

        locationReference.child(model.getLuogo()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Place place = dataSnapshot.getValue(Place.class);
                        assert place != null;
                        holder.luogo.setText(place.getName() + ", " + place.getAddress());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        //holder.descrizione.setText(model.getDescrizione());

        //holder.durata.setText(model.getDurata());

        holder.costo.setText(model.getCosto() + "???");

        if (model.isPrivate() && hidePrivate) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    // Function to tell the class about the Card view in
    // which the data will be shown
    @NonNull
    @Override
    public matchesViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_layout, parent, false);
        return new matchesAdapter.matchesViewHolder(view);
    }

    public interface ItemClickListener {
        void onItemClick(Match match);
    }

    // Sub Class to create references of the views in Card
    // view
    static class matchesViewHolder extends RecyclerView.ViewHolder {
        TextView titolo, luogo, partecipanti, numeroSquadre, dateTime, sport,
                descrizione, durata, costo;

        public matchesViewHolder(@NonNull View itemView) {
            super(itemView);

            titolo = itemView.findViewById(R.id.titolo);
            luogo = itemView.findViewById(R.id.luogo);
            //partecipanti = itemView.findViewById(R.id.partecipanti);
            //numeroSquadre = itemView.findViewById(R.id.numeroSquadre);
            dateTime = itemView.findViewById(R.id.data);
            //sport = itemView.findViewById(R.id.sport);
            //descrizione = itemView.findViewById(R.id.descrizione);
            //durata = itemView.findViewById(R.id.durata);
            costo = itemView.findViewById(R.id.costo);
        }
    }
}
