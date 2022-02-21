package it.unimib.pickapp.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_item_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getFullName().setText(users.get(position).getFullname());
        viewHolder.getNickname().setText(users.get(position).getNickname());
        viewHolder.getEmail().setText(users.get(position).getEmail());
        viewHolder.getExperience().setText(Double.toString(users.get(position).getExperienceLevel()));
        viewHolder.getReliability().setText(Double.toString(users.get(position).getReliabilityLevel()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView fullName;
        private final TextView nickname;
        private final TextView email;
        private final TextView experience;
        private final TextView reliability;

        public ViewHolder(View view) {
            super(view);

            fullName = (TextView) view.findViewById(R.id.name);
            nickname = (TextView) view.findViewById(R.id.nickname);
            email = (TextView) view.findViewById(R.id.email);
            experience = (TextView) view.findViewById(R.id.experience);
            reliability = (TextView) view.findViewById(R.id.reliability);
        }

        public TextView getFullName() {
            return fullName;
        }

        public TextView getNickname() {
            return nickname;
        }

        public TextView getEmail() {
            return email;
        }

        public TextView getExperience() {
            return experience;
        }

        public TextView getReliability() {
            return reliability;
        }
    }
}
