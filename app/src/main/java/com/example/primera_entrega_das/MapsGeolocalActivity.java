package com.example.primera_entrega_das;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsGeolocalActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient proveedordelocalizacion;
    private double lat, longi;
    private TextView textlat, textlongi;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_geolocal);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapGeo);
        // Se verifica si mMap ya está inicializado
        if (mMap == null) {
            mapFragment.getMapAsync(this);
        }

        //Encontrar TextViews:
        textlat = findViewById(R.id.textViewLat);
        textlongi = findViewById(R.id.textViewLongi);

        // Inicializar el cliente de ubicación fusionada
        proveedordelocalizacion = LocationServices.getFusedLocationProviderClient(this);

        // Solicitar permisos de ubicación en caso de no estar concedidos
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            obtenerUbicacion();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings uiSettings = mMap.getUiSettings();
        //Añadir zoom out/in para manejar mejor el mapa
        uiSettings.setZoomControlsEnabled(true);

    }

    private void obtenerUbicacion() {

        //Obtener la ultima ubicación conocida del usuario
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
        proveedordelocalizacion.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    longi = location.getLongitude();
                    LatLng posicionActual = new LatLng(lat, longi);
                    Log.d("Geo", "bien");
                    // Añadir valores a lso textviews con la latitud y longitud
                    textlat.setText("Latitud: " + String.valueOf(lat));
                    textlongi.setText("Longitud: " + String.valueOf(longi));
                    //Añadir marcador personalizado
                    //mMap.addMarker(new MarkerOptions().position(posicionActual).title("Estoy aqui!!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    // Obtener un BitmapDescriptor del vector asset y ajustar su tamaño

                    mMap.addMarker(new MarkerOptions()
                            .position(posicionActual)
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.baseline_person_24_superred))
                            .title("Estoy aqui!!"));
                    //Mover la camara a esa posicion
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionActual, 15));
                    // Habilitar mi ubicación si el permiso esta concedido
                    if (ContextCompat.checkSelfPermission(MapsGeolocalActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Log.d("maps", "Posicion desconocida");
                    textlat.setText("Latitud: Coordenadas no encontradas");
                    textlongi.setText("Longitud: Coordenadas no encontradas");
                }
            }
        });



    }


    public void OnClickGeoVolver(View v){
        //Al darle click te lleva a la pagina main de la aplicación
        Log.d("Geo","Sale de la pantalla");
        Intent geo = new Intent(this, MainActivity.class);
        this.startActivity(geo);
        finish();
    }

    //Metodo para pedir permiso la primera vez que entra a la pantalla de geolocalización
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            }
        } else {
            // Mostrar mensaje por pantalla
            Toast.makeText(this, "El permiso de ubicacion ha sido rechazado", Toast.LENGTH_SHORT).show();
        }

    }

    //Codigo para crear un marcador personalizado
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}