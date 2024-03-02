package it.univaq.labprog.piattaformegas;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import it.univaq.labprog.piattaformegas.databinding.FragmentMapBinding;
import it.univaq.labprog.piattaformegas.model.Platform;
import it.univaq.labprog.piattaformegas.service.LocationHelper;
import it.univaq.labprog.piattaformegas.service.MainViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener{
    private GoogleMap map;
    private Marker marker;
    private List<Platform> platforms = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private MainViewModel mainViewModel;
    private FragmentMapBinding binding;

    private ActivityResultLauncher<String> permissionLaucher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean o) {
                    if(o) {
                        LocationHelper.start(requireContext(), MapFragment.this);
                    } else {
                        Toast.makeText(requireContext(),
                                        R.string.location_required,
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
    );
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.fragmentMap);
        mapFragment.getMapAsync(this);

        int fineLocation = ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (fineLocation == PackageManager.PERMISSION_DENIED) {
            permissionLaucher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        int fineLocation = ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (fineLocation == PackageManager.PERMISSION_GRANTED) {
            LocationHelper.start(requireContext(), this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LocationHelper.stop(requireContext(), this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                Platform platform = (Platform) marker.getTag();
                if(platform != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DetailActivity.EXTRA_PLATFORM, platform);
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_menu_map_to_detailActivity, bundle);
                }

            }
        });

        showMarkers();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        bounds.include(currentPosition);

        if (marker == null) {
            MarkerOptions opt = new MarkerOptions();
            opt.title("My Location");
            opt.position(currentPosition);
            marker = map.addMarker(opt);
        } else {
            marker.setPosition(currentPosition);
        }

        new Thread(() -> {
            if(!platforms.isEmpty()) {

                for(Platform platform : platforms) {
                    Location platformLocation = new Location("Platform");
                    platformLocation.setLatitude(platform.getLatitudine());
                    platformLocation.setLongitude(platform.getLongitudine());
                    if (platformLocation.distanceTo(location) >= 10000) continue;
                    bounds.include(new LatLng(platform.getLatitudine(), platform.getLongitudine()));

                }
            }
            binding.getRoot().post(() -> {
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 16));
            });

        }).start();


    }

    private void showMarkers() {
        mainViewModel.getPlatforms()
                .observe(getViewLifecycleOwner(), platforms -> {

                    MapFragment.this.platforms = platforms;
                    map.clear();
                    platforms.forEach(this::createPlatform);
                });
    }

    private void createPlatform(Platform platform) {
        MarkerOptions options = new MarkerOptions();
        options.title(platform.getDenominazione());
        options.position(new LatLng(platform.getLatitudine(), platform.getLongitudine()));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker = map.addMarker(options);
        marker.setTag(platform);
        markers.add(marker);
    }
}
