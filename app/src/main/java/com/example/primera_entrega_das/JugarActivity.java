package com.example.primera_entrega_das;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class JugarActivity extends AppCompatActivity {

    private ArrayList<ModeloPregunta> listaPreguntasTemas;

    public static class HorizontalFragment extends Fragment {
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_jugar_landscape, container, false);
            return v;
        }
    }

    public static class VerticalFragment extends Fragment {
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_jugar_portrait, container, false);
            return v;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_jugar);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Cargar el Fragmento para pantallas grandes en modo horizontal
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.elFragmento, new HorizontalFragment())
                    .commit();
        } else {

            // Cargar el Fragmento para pantallas pequeñas en modo vertical
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.elFragmento, new VerticalFragment())
                    .commit();
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String tema = extras.getString("TemaJugar");

            /*
            OperacionesBD bd = new OperacionesBD(this,1);
            listaPreguntasTemas = bd.obtenerPreguntaTema(tema);

            // Indice aleatorio para obtener una pregunta aleatoria
            Random random = new Random();
            int iAleatorio = random.nextInt(listaPreguntasTemas.size());

            // Manera de obtener la pregunta correspondiente al índice aleatorio
            ModeloPregunta mPregunta = listaPreguntasTemas.get(iAleatorio);



            Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getPregunta());
            Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getRespuesta1());
            Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getRespuesta2());
            Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getRespuesta3());
            Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getRespuestaCorrecta());
            Log.d("PREGUNTA SOBRE TEMA", String.valueOf(listaPreguntasTemas.size()));
            //Funciona
            // Habria que recorrer todas las preguntas, seleccionar una de manera aleatoria y mirar
            //la correcta
            //Guardar Pregunta en un fichero ?

             */
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        View frag = super.getSupportFragmentManager().findFragmentById(R.id.elFragmento).getView();

        //puedes dar estilos, buscar botones, textviews etc
        //bajar aqui lo de buscar para meter el texto en la checkbox
    }

}