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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class JugarActivity extends AppCompatActivity {

    protected ArrayList<ModeloPregunta> listaPreguntasTemas, listaPreguntasTodas;
    private final int preguntasMostradas = 0;
    private final int respuestasCorrectas = 0;
    private int contadorPreguntasMostradas = 0;

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
        //Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getRespuestaCorrecta());
        //Log.d("PREGUNTA SOBRE TEMA", String.valueOf(listaPreguntasTemas.size()));
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


            OperacionesBD bd = new OperacionesBD(this, 1);
            if (extras != null) {
                String tema = extras.getString("TemaJugar");
                listaPreguntasTemas = bd.obtenerPreguntaTema(tema);
                ModeloPregunta mPregunta = this.obtenerPreguntaLista(listaPreguntasTemas);
                //puedes dar estilos, buscar botones, textviews etc
                //bajar aqui lo de buscar para meter el texto en la checkbox

                textViewPregunta.setText(mPregunta.getPregunta());
                radioButtonR1.setText(mPregunta.getRespuesta1());
                radioButtonR2.setText(mPregunta.getRespuesta2());
                radioButtonR3.setText(mPregunta.getRespuesta3());

            }else{ //preguntas aleatorias

                listaPreguntasTodas = bd.obtenerPreguntaRandom();
                ModeloPregunta mPregunta = this.obtenerPreguntaLista(listaPreguntasTodas);

                textViewPregunta.setText(mPregunta.getPregunta());
                radioButtonR1.setText(mPregunta.getRespuesta1());
                radioButtonR2.setText(mPregunta.getRespuesta2());
                radioButtonR3.setText(mPregunta.getRespuesta3());
            }
            //me falta que saque preguntas aleatorias de todos los temas
            // y la verificacion de la pregunta y la noti
            //hacer inserciones en la bd desde un fichero de texto
            //preferencias con tema e idioma
            btnSigPregunta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contadorPreguntasMostradas < 3) {
                        // Mostrar nueva pregunta
                        ModeloPregunta mPregunta = obtenerPreguntaLista(listaPreguntasTemas);

                        // Resto de tu código para mostrar la pregunta en los radio buttons, etc.

                        // Incrementar el contador
                        contadorPreguntasMostradas++;
                    } else {
                        // Ya se mostraron 3 preguntas, puedes mostrar un mensaje o realizar otra acción
                        Toast.makeText(JugarActivity.this, "Ya se mostraron 3 preguntas", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    // cargar pregunta da igual que sea aleatoria o de tema
    private String cargarNuevaPregunta(ArrayList<ModeloPregunta> listaPreguntas, TextView tV1, RadioButton rB1, RadioButton rB2, RadioButton rB3) {
        ModeloPregunta mPregunta = this.obtenerPreguntaLista(listaPreguntas);
        // Resto del código para configurar los elementos de la interfaz
        tV1.setText(mPregunta.getPregunta());
        rB1.setText(mPregunta.getRespuesta1());
        rB2.setText(mPregunta.getRespuesta2());
        rB3.setText(mPregunta.getRespuesta3());
        String respuesta = mPregunta.getRespuestaCorrecta();
        return respuesta;
    }

    //clase para obtener una pregunta aleatoria dada una lista cualquiera, ya sea en base a un tema o no.
    public ModeloPregunta obtenerPreguntaLista(ArrayList<ModeloPregunta> listaPreguntas) {

        Random random = new Random();
        int iAleatorio = random.nextInt(listaPreguntas.size());
        boolean enc = false;

        iAleatorio = random.nextInt(listaPreguntas.size());

        // Devolver la pregunta correspondiente al índice aleatorio
        // (puede repetirse una pregunta en caso de que haya pocas preguntas en la BD)
        return listaPreguntas.get(iAleatorio);
    }



    // Método para obtener la respuesta seleccionada de los RadioButtons (gpt)
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