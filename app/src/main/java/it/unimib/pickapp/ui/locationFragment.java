package it.unimib.pickapp.ui;
import static it.unimib.pickapp.repository.Constants.MAPVIEW_BUNDLE_KEY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import it.unimib.pickapp.R;


public class locationFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private final String TAG = "locationFragment";

    public locationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        setTitle(view);

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

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(@NonNull GoogleMap map) {

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
                Log.d(TAG, "location != null");
                MapsInitializer.initialize(getContext());
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude+0.011, longitude+0.019))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) // null = default icon
                        .title("Partita di tennis"));
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude-0.017, longitude+0.0102))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)) // null = default icon
                        .title("Partita di basket"));
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude+0.023, longitude+0.01))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)) // null = default icon
                        .title("Partita di calcio"));
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude-0.0104, longitude-0.0205))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) // null = default icon
                        .title("Partita di tennis"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastPosition, 13));
            } else {
                Toast.makeText(getActivity(), R.string.enable_gps, Toast.LENGTH_SHORT).show();
            }
        });
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

    public void setTitle(View view){
        TextView titleToolbar = view.findViewById(R.id.titleHome);
        String title = "Location";
        titleToolbar.setText(title);
    }
}