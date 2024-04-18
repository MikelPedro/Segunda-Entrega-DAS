package com.example.primera_entrega_das;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.RadioButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.primera_entrega_das.databinding.ActivityMapsJuegoBinding;

public class MapsJuegoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap elmapa;
    private ActivityMapsJuegoBinding binding;

    private double lat;
    private double longi;
    private String respReal = "";

    private RadioButton radioButtonR1, radioButtonR2, radioButtonR3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsJuegoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        radioButtonR1 = findViewById(R.id.radioButtonMapa1);
        radioButtonR2 = findViewById(R.id.radioButtonMapa2);
        radioButtonR3 = findViewById(R.id.radioButtonMapa3);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        elmapa = googleMap;
        elmapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.mapa_estilo));
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        elmapa.addMarker(new MarkerOptions().position(sydney).title("¿Dónde estoy?"));
        elmapa.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Crear una cámara con zoom y movimiento hacia la posición del marcador
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sydney)
                .zoom(10)
                .build();

        // Mover la cámara hacia la posición del marcador con animación
        elmapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private String cargarPregunta(CiudadPregunta pregunta, int imagen) {
        radioButtonR1.setText(pregunta.getCiudad1());
        radioButtonR2.setText(pregunta.getCiudad2());
        radioButtonR3.setText(pregunta.getCiudad3());
        respReal = pregunta.getCiudadCorrecta();
        return respReal;
    }

    private double obtenerLatitud(CiudadPregunta pregunta){
        return pregunta.getLatitud();
    }

    private double obtenerLongitud(CiudadPregunta pregunta){
        return pregunta.getLongitud();
    }

    // Método para obtener la respuesta seleccionada de los RadioButtons
    private String obtenerRespuestaSeleccionada(RadioButton radioButtonR1, RadioButton radioButtonR2, RadioButton radioButtonR3) {
        if (radioButtonR1.isChecked()) {
            return radioButtonR1.getText().toString();
        } else if (radioButtonR2.isChecked()) {
            return radioButtonR2.getText().toString();
        } else if (radioButtonR3.isChecked()) {
            return radioButtonR3.getText().toString();
        }
        return "";
    }
}