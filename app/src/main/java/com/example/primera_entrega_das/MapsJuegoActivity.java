package com.example.primera_entrega_das;

import androidx.fragment.app.FragmentActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.primera_entrega_das.databinding.ActivityMapsJuegoBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;


public class MapsJuegoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap elmapa;
    private ActivityMapsJuegoBinding binding;

    private double lat;
    private double longi;
    private String respReal = "";

    private RadioButton radioButtonR1, radioButtonR2, radioButtonR3;
    private Button btnSigMap;

    private CiudadPregunta cPregunta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsJuegoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        radioButtonR1 = findViewById(R.id.radioButtonMapa1);
        radioButtonR2 = findViewById(R.id.radioButtonMapa2);
        radioButtonR3 = findViewById(R.id.radioButtonMapa3);
        btnSigMap = findViewById(R.id.btnMapSig);

        //Obtener la informacion guardada en el intent
        super.getIntent().putExtra("idPregunta", super.getIntent().getExtras().getInt("idPregunta"));
        super.getIntent().putExtra("pregCorrecta",super.getIntent().getExtras().getInt("pregCorrecta"));
        super.getIntent().putExtra("pregRespondida",super.getIntent().getExtras().getInt("pregRespondida"));

        OperacionesBDMapas bd = new OperacionesBDMapas(this, 1);
        //obtener un objeto ciudadPregunta en base al id que se obtiene del intent
        cPregunta = bd.obtenerPregPorId(super.getIntent().getExtras().getInt("idPregunta"));

        //devolver resultado correcto de la pregunta

        respReal = cargarPregunta(cPregunta);

        // Obtener el mapa asíncronamente
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        elmapa = googleMap;
        elmapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.mapa_estilo));
        // Add a marker in Sydney and move the camera
        /*
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
        */
        //obtener un objeto ciudadPregunta en base al id que se obtiene del intent
        //cPregunta = bd.obtenerMapaPorId(super.getIntent().getExtras().getInt("idPregunta"));
        //this.obtCiudad(super.getIntent().getExtras().getInt("idPregunta"));
        //devolver resultado correcto de la pregunta
        //respReal = this.obtCiudad(super.getIntent().getExtras().getInt("idPregunta");

    }

    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        OperacionesBDMapas bd = new OperacionesBDMapas(this, 1);
        //cargar info de la pregunta en el view
        cargarPregunta(bd.obtenerPregPorId(extras.getInt("idPregunta")));


        btnSigMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String respUsu = obtenerRespuestaSeleccionada(radioButtonR1, radioButtonR2, radioButtonR3);

                //Comparar la respuesta real con la del usuario
                if (respReal.equals(respUsu)) {
                    //sumar 1 punto al contador de preguntas correctas
                    MapsJuegoActivity.super.getIntent().putExtra("pregCorrecta", MapsJuegoActivity.super.getIntent().getExtras().getInt("pregCorrecta") + 1);
                }

                //sumar 1 punto al contador de preguntas respondidas
                MapsJuegoActivity.super.getIntent().putExtra("pregRespondida", MapsJuegoActivity.super.getIntent().getExtras().getInt("pregRespondida") + 1);

                Log.d("PREGUNTAS RESPONDIDAS", String.valueOf(MapsJuegoActivity.super.getIntent().getExtras().getInt("pregRespondida")));

                if (MapsJuegoActivity.super.getIntent().getExtras().getInt("pregRespondida") == 3) {

                    //si es igual a 3 se acaba el juego y se muestra una nueva actividad con el resultado
                    Intent intent = new Intent(MapsJuegoActivity.this, FinJuegoActivity.class);
                    //Añadir al intent informacion sobre las preguntas acertadas y respondidas
                    intent.putExtra("pregCorrecta", MapsJuegoActivity.super.getIntent().getExtras().getInt("pregCorrecta"));
                    intent.putExtra("pregRespondida", MapsJuegoActivity.super.getIntent().getExtras().getInt("pregRespondida"));

                    // Limpia el historial de preguntas que han aparecido en la partida
                    OperacionesBD.vaciarHistorial();

                    MapsJuegoActivity.super.startActivity(intent);
                    finish(); //finaliza esta actividad
                } else {


                    Intent intent = new Intent(MapsJuegoActivity.this, MapsJuegoActivity.class);
                    intent.putExtra("idPregunta", bd.obtenerPregRandom());
                    intent.putExtra("pregCorrecta", MapsJuegoActivity.super.getIntent().getExtras().getInt("pregCorrecta"));
                    intent.putExtra("pregRespondida", MapsJuegoActivity.super.getIntent().getExtras().getInt("pregRespondida"));

                    MapsJuegoActivity.super.startActivity(intent);
                    finish();
                }

            }
        });
    }


    private String cargarPregunta(CiudadPregunta pregunta) {
        radioButtonR1.setText(pregunta.getCiudad1());
        radioButtonR2.setText(pregunta.getCiudad2());
        radioButtonR3.setText(pregunta.getCiudad3());
        respReal = pregunta.getCiudadCorrecta();

        // Actualizar la posición del marcador en el mapa con la nueva ubicación
        LatLng nuevaPosicion = new LatLng(pregunta.getLatitud(), pregunta.getLongitud());
        elmapa.clear(); // Limpiar el mapa antes de agregar el nuevo marcador
        elmapa.addMarker(new MarkerOptions().position(nuevaPosicion).title("¿Dónde estoy?"));
        elmapa.moveCamera(CameraUpdateFactory.newLatLng(nuevaPosicion));

        // Crear una cámara con zoom y movimiento hacia la posición del marcador
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(nuevaPosicion)
                .zoom(10)
                .build();

        // Mover la cámara hacia la posición del marcador con animación
        elmapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        return respReal;
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