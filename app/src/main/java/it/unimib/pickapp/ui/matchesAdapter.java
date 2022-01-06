package it.unimib.pickapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class matchesAdapter extends FirebaseRecyclerAdapter<Match, matchesAdapter.matchesViewHolder> {

    public matchesAdapter(
            @NonNull FirebaseRecyclerOptions<Match> options)
    {
        super(options);
    }

    // Function to bind the view in Card view with data in
    // model class
    @Override
    protected void
    onBindViewHolder(@NonNull matchesViewHolder holder,
                     int position, @NonNull Match model) {

        holder.titolo.setText(model.getTitolo());

        holder.luogo.setText(model.getLuogo());

        holder.partecipanti.setText(model.getPartecipanti());

        holder.numeroSquadre.setText(model.getNumeroSquadre());

        holder.dateTime.setText(model.getDateTime());

        holder.sport.setText(model.getSport());

        holder.descrizione.setText(model.getDescrizione());

        holder.durata.setText(model.getDurata());

        holder.costo.setText((int) model.getCosto());
    }

    // Function to tell the class about the Card view in
    // which the data will be shown
    @NonNull
    @Override
    public matchesViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_layout, parent, false);
        return new matchesAdapter.matchesViewHolder(view);
    }

    // Sub Class to create references of the views in Card
    // view
    class matchesViewHolder extends RecyclerView.ViewHolder {
        TextView titolo, luogo, partecipanti, numeroSquadre, dateTime, sport,
                descrizione, durata, costo;
        public matchesViewHolder(@NonNull View itemView)
        {
            super(itemView);

            titolo = itemView.findViewById(R.id.titolo);
            luogo = itemView.findViewById(R.id.luogo);
            partecipanti = itemView.findViewById(R.id.partecipanti);
            numeroSquadre = itemView.findViewById(R.id.numeroSquadre);
            dateTime = itemView.findViewById(R.id.dateTime);
            sport = itemView.findViewById(R.id.sport);
            descrizione = itemView.findViewById(R.id.descrizione);
            durata = itemView.findViewById(R.id.durata);
            costo = itemView.findViewById(R.id.costo);
        }
    }
}