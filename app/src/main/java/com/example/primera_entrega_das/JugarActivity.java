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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class JugarActivity extends AppCompatActivity {

    protected ArrayList<ModeloPregunta> listaPreguntasTemas;

    //Parte necesaria para fragments
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
            // Cargar el Fragmento en modo horizontal
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.elFragmento, new HorizontalFragment())
                    .commit();
        } else {

            // Cargar el Fragment en modo vertical
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.elFragmento, new VerticalFragment())
                    .commit();
        }

        //Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getPregunta());
        //Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getRespuesta1());
        //Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getRespuesta2());
        //Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getRespuesta3());
        //Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getRespuestaCorrecta());
        //Log.d("PREGUNTA SOBRE TEMA", String.valueOf(listaPreguntasTemas.size()));
        //Funciona
        // Habria que recorrer todas las preguntas, seleccionar una de manera aleatoria y mirar
        //la correcta
        //Guardar Pregunta en un fichero ?

    }




    @Override
    protected void onStart() {
        super.onStart();

        View frag = super.getSupportFragmentManager().findFragmentById(R.id.elFragmento).getView();

        Bundle extras = getIntent().getExtras();
        if (frag != null) {
            RadioButton radioButtonR1 = frag.findViewById(R.id.radioButtonJugar1);
            RadioButton radioButtonR2 = frag.findViewById(R.id.radioButtonJugar2);
            RadioButton radioButtonR3 = frag.findViewById(R.id.radioButtonJugar3);
            TextView textViewPregunta = frag.findViewById(R.id.textViewPreguntaJugar);
            Button btnSigPregunta = frag.findViewById(R.id.botonSigPreg);

            if (extras != null) {
                String tema = extras.getString("TemaJugar");


                OperacionesBD bd = new OperacionesBD(this, 1);
                listaPreguntasTemas = bd.obtenerPreguntaTema(tema);

                ModeloPregunta mPregunta = this.obtenerPreguntaLista(listaPreguntasTemas);
                //puedes dar estilos, buscar botones, textviews etc
                //bajar aqui lo de buscar para meter el texto en la checkbox

                textViewPregunta.setText(mPregunta.getPregunta());
                radioButtonR1.setText(mPregunta.getRespuesta1());
                radioButtonR2.setText(mPregunta.getRespuesta2());
                radioButtonR3.setText(mPregunta.getRespuesta3());

            }

            //me falta que saque preguntas aleatorias de todos los temas
            // y la verificacion de la pregunta y la noti
        }




    }

    //clase para obtener una pregunta aleatoria dada una lista cualquiera, ya sea en base a un tema o no.
    public ModeloPregunta obtenerPreguntaLista(ArrayList<ModeloPregunta> listaPreguntas) {

        Random random = new Random();
        int iAleatorio = random.nextInt(listaPreguntas.size());

        // Devolver la pregunta correspondiente al índice aleatorio
        return listaPreguntas.get(iAleatorio);
    }


}