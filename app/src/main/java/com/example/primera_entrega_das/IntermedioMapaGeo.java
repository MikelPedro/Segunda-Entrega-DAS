package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class IntermedioMapaGeo extends AppCompatActivity {

    //Clase para la pantalla intermedia entre el juego del mapa y la opcion de geolocalizacion
    private String tema;

    private int imagenId;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedio_mapa_geo);

        imageView = findViewById(R.id.imageMapa);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tema = extras.getString("Tema");
            imagenId = extras.getInt("Imagen");

            // Carga la imagen en el ImageView
            imageView.setImageResource(imagenId);

            TextView temaTextView = findViewById(R.id.textViewTemaInterMapa);

            temaTextView.setText(String.format("Tema: %s", tema));
        }
    }

    public void OnClickJugarMapa(View v){
        Log.d("Intermedio","ENTRA");
        OperacionesBDMapas opBDMap = new OperacionesBDMapas(this,1);

        Intent mapas = new Intent(this, MapsJuegoActivity.class);
        Log.d("Intermedio", String.valueOf(opBDMap.obtenerPregRandom()));
        mapas.putExtra("idPregunta", opBDMap.obtenerPregRandom());
        mapas.putExtra("pregCorrecta", 0);
        mapas.putExtra("pregRespondida", 0);
        this.startActivity(mapas);
        finish();
    }

    public void OnClickGeoMapOpcion(View v){
        Log.d("Geo","Entra desde el intermedio");
        Intent mapas = new Intent(this, MapsGeolocalActivity.class);
        this.startActivity(mapas);
        finish();
    }


}