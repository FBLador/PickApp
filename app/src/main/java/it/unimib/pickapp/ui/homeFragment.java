package it.unimib.pickapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.unimib.pickapp.R;

/**
 * It shows the homepage of the app.
 */
public class homeFragment extends Fragment {

    private TextView titleToolbar;
    private final String title = "Home";

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        setTitle(view);

        return view;
    }

    public void setTitle(View view){
        titleToolbar = view.findViewById(R.id.titleHome);
        titleToolbar.setText(title);
    }
}