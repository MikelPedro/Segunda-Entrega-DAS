package com.example.primera_entrega_das;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.primera_entrega_das.databinding.ActivityMapsGeolocalBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsGeolocalActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsGeolocalBinding binding;
    private FusedLocationProviderClient proveedordelocalizacion;
    private Marker currentLocationMarker;
    private TextView textlat, textlongi;
    private LocationCallback actualizador;
    private LocationRequest peticion;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsGeolocalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Encontrar TextViews:
        textlat = findViewById(R.id.textViewLat);
        textlongi = findViewById(R.id.textViewLongi);

        //Instanciar proveedor de posiciones
        proveedordelocalizacion = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapGeo);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // Pedir permisos de ubicación en caso de no estar concedidos
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }


        proveedordelocalizacion.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d("GEO","llega aqui");
                    textlat.setText("Latitud: " + location.getLatitude());
                    textlongi.setText("Longitud: " + location.getLongitude());
                    LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                    // Personalizar el icono del marcador
                    mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicación").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 15));
                } else {
                    textlat.setText("Latitud: (desconocida)");
                    textlongi.setText("Longitud: (desconocida)");
                    Log.d("maps", "Posicion desconocida");
                }
            }
        });

        // Habilitar la capa de mi ubicación
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }


    }

    private void actualizarMarcador(LocationResult locationResult){
        if (locationResult != null) {
            Location location = locationResult.getLastLocation();
            LatLng posicion = new LatLng(location.getLatitude(), location.getLongitude());
            if (currentLocationMarker == null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(posicion);
                markerOptions.title("Estoy aquí!!");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                currentLocationMarker = mMap.addMarker(markerOptions);
            } else {
                currentLocationMarker.setPosition(posicion);
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15));
        }
    }

    private void actualizarPosicion(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        proveedordelocalizacion.requestLocationUpdates(peticion, actualizador, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarPosicion();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Detener la captura de los cambios de posición
        proveedordelocalizacion.removeLocationUpdates(actualizador);
    }

    public void OnClickGeoVolver(View v){
        Log.d("mapa","SALE");
        Intent geo = new Intent(this, MainActivity.class);
        this.startActivity(geo);
        finish();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso de ubicación concedido, habilitar la capa de mi ubicación
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                // Permiso de ubicación denegado, mostrar mensaje al usuario
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

}