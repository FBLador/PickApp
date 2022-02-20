package it.unimib.pickapp.ui;
import static it.unimib.pickapp.Constants.MAPVIEW_BUNDLE_KEY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import it.unimib.pickapp.R;
import it.unimib.pickapp.model.Match;


public class locationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private MapView mMapView;
    private final String TAG = "locationFragment";
    private DatabaseReference mbase;
    List<Match> listaPartite = new ArrayList<>();
    private boolean markersDrawn = false;


    public locationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        setTitle(view, getString(R.string.location));

        if(!markersDrawn) {
            mbase = FirebaseDatabase.getInstance().getReference("Matches");
            mbase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot partite : snapshot.getChildren()) {
                        Match partita = new Match();
                        partita.setId(partite.child("id").getValue().toString());
                        partita.setTitolo(partite.child("titolo").getValue().toString());
                        partita.setSport(partite.child("sport").getValue().toString());
                        partita.setDate(partite.child("date").getValue().toString());
                        partita.setTime(partite.child("time").getValue().toString());
                        partita.setLuogo(partite.child("luogo").getValue().toString());
                        partita.setNumeroSquadre(Integer.parseInt(partite.child("numeroSquadre").getValue().toString()));
                        partita.setCosto(Double.parseDouble(partite.child("costo").getValue().toString()));
                        partita.setPrivate(Boolean.parseBoolean(partite.child("private").getValue().toString()));
                        partita.setDescrizione(partite.child("descrizione").getValue().toString());
                        partita.setCreatorId(partite.child("creatorId").getValue().toString());
                        listaPartite.add(partita);
                        Log.d(TAG, "listaPartite.size() = " + listaPartite.size());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        mMapView = view.findViewById(R.id.matches_list_map);
        initGoogleMap(savedInstanceState);

        return view;
    }


    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }


    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Log.d(TAG, "registerForActivityResult: GRANTED");
                    mMapView.getMapAsync(this);
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Log.d(TAG, "registerForActivityResult: DENIED");
                }
            });



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.map_style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(requireContext());

        // Controllo se l'utente ha già accettato i permessi
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // Permessi accettati.
            Log.d(TAG, "onMapReady: GRANTED");
            setupMap(map, client);
        } else {
            Log.d(TAG, "onMapReady: DENIED");
            // Permessi negati. Posso chiederli direttamente
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        }

    }

    @SuppressLint("MissingPermission")
    public void setupMap(GoogleMap map, FusedLocationProviderClient client) {
        map.setMyLocationEnabled(true);
        client.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if(location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng lastPosition = new LatLng(latitude, longitude);
                MapsInitializer.initialize(getContext());
                createRandomMarkers(listaPartite.size(), map, latitude, longitude);
                map.setOnInfoWindowClickListener(this);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPosition, 14));
            } else {
                Toast.makeText(getActivity(), R.string.enable_gps, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void createRandomMarkers(int markersQuantity, GoogleMap map, double latitude, double longitude) {
        // Randomizziamo la posizione delle partite per test, altrimenti basta prendere le coordinate dal db
        Random rand = new Random(3);
        for(int i=0; i < markersQuantity; i++) {
            switch(listaPartite.get(i).getSport()) {
                case "TENNIS":
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude+((rand.nextDouble()/50)-(rand.nextDouble()/50)), longitude+((rand.nextDouble()/90)-(rand.nextDouble()/90))))
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_sports_tennis_24)) // null = default icon
                            .title(listaPartite.get(i).getTitolo())
                            .alpha(1f)
                            .snippet(listaPartite.get(i).getDate() + ", " + listaPartite.get(i).getTime()));
                    break;
                case "BASKETBALL":
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude+((rand.nextDouble()/50)-(rand.nextDouble()/50)), longitude+((rand.nextDouble()/50)-(rand.nextDouble()/50))))
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_sports_basketball_24)) // null = default icon
                            .title(listaPartite.get(i).getTitolo())
                            .alpha(1f)
                            .snippet(listaPartite.get(i).getDate() + ", " + listaPartite.get(i).getTime()));
                    break;
                case "SOCCER":
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude+((rand.nextDouble()/50)-(rand.nextDouble()/50)), longitude+((rand.nextDouble()/50)-(rand.nextDouble()/50))))
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_sports_soccer_24)) // null = default icon
                            .title(listaPartite.get(i).getTitolo())
                            .alpha(1f)
                            .snippet(listaPartite.get(i).getDate() + ", " + listaPartite.get(i).getTime()));
                    break;
                case "FOOTBALL":
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude+((rand.nextDouble()/50)-(rand.nextDouble()/50)), longitude+((rand.nextDouble()/50)-(rand.nextDouble()/50))))
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_baseline_sports_football_24)) // null = default icon
                            .title(listaPartite.get(i).getTitolo())
                            .alpha(1f)
                            .snippet(listaPartite.get(i).getDate() + ", " + listaPartite.get(i).getTime()));
                    break;
            }
        }
        markersDrawn = true;
    }


    // Toolbar
    public void setTitle(View view, String title){
        TextView titleToolbar = view.findViewById(R.id.titleLocation);
        titleToolbar.setText(title);
    }


    // This converts a Vector image to bitmapDescriptor (so it can be used as an icon for the markers on the map)
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Match match = new Match();
        for(int i=0; i < listaPartite.size(); i++) {
            if(listaPartite.get(i).getTitolo().equals(marker.getTitle())) {
                match = listaPartite.get(i);
            }
        }
        MatchViewModel matchViewModel =
                new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
        matchViewModel.setMatch(match);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_location_to_matchFragment);
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }


    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }


    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}