package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class JugarActivity extends AppCompatActivity {
    private ArrayList<ModeloPregunta> listaPreguntasTemas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String tema = extras.getString("TemaJugar");


            OperacionesBD bd = new OperacionesBD(this,1);
            listaPreguntasTemas = bd.obtenerPreguntaTema(tema);
            Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(0).getPregunta());
            Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(0).getRespuesta1());
            Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(0).getRespuesta2());
            Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(0).getRespuesta3());
            Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(0).getRespuestaCorrecta());
            Log.d("PREGUNTA SOBRE TEMA", String.valueOf(listaPreguntasTemas.size()));
            //Funciona
            // Habria que recorrer todas las preguntas, seleccionar una de manera aleatoria y mirar
            //la correcta
        }


    }
}