package com.example.primera_entrega_das;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;


public class JugarActivity extends AppCompatActivity {

    protected ArrayList<ModeloPregunta> listaPreguntasTemas, listaPreguntasTodas;
    private static final String KEY_CONTADOR = "contPreguntasMostradas"; //chatgpt
    private static final String KEY_CONT_ACIERTO = "contPreguntasAcierto";

    private boolean primeraPreguntaMostrada = false;

    private int contPreguntasAcierto = 0;
    private int contPreguntasMostradas = 0;
    private String respReal;

    private RadioButton radioButtonR1, radioButtonR2, radioButtonR3;
    private TextView textViewPregunta;

    private Button btnSigPregunta;

    private JugarViewModel jugarViewModel;

    //Parte necesaria para fragments
    /*
    public static class HorizontalFragment extends Fragment {

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_jugar_landscape, container, false);
            // Realiza tus operaciones habituales
            return v;
        }
    }

    public static class VerticalFragment extends Fragment {

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_jugar_portrait, container, false);
            // Realiza tus operaciones habituales
            return v;
        }
    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_jugar);
        jugarViewModel = new ViewModelProvider(this).get(JugarViewModel.class); //chatgpt


        radioButtonR1 = findViewById(R.id.radioButtonJugar1);
        radioButtonR2 = findViewById(R.id.radioButtonJugar2);
        radioButtonR3 = findViewById(R.id.radioButtonJugar3);
        textViewPregunta = findViewById(R.id.textViewPreguntaJugar);
        btnSigPregunta = findViewById(R.id.botonSigPreg);
        /*
        try {
            ModeloPregunta preguntaAlmacenada = jugarViewModel.getPregunta();
            if (preguntaAlmacenada != null) {
                // Restaurar la pregunta almacenada en el ViewModel
                respReal = cargarPregunta(preguntaAlmacenada);
                contPreguntasMostradas = savedInstanceState.getInt(KEY_CONTADOR, 0);
                contPreguntasAcierto = savedInstanceState.getInt(KEY_CONT_ACIERTO, 0);
            }
            //No hay pregunta almacenada proseguir
        } catch (Exception e) {
            // excepción
            e.printStackTrace();
        }

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
        */
        //Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getPregunta());
        //Log.d("PREGUNTA SOBRE TEMA",listaPreguntasTemas.get(iAleatorio).getRespuestaCorrecta());
        //Log.d("PREGUNTA SOBRE TEMA", String.valueOf(listaPreguntasTemas.size()));
        //Guardar Pregunta en un fichero ?
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Cargar recursos específicos de landscape
            setContentView(R.layout.activity_jugar_landscape);
        } else {
            // Cargar recursos específicos de portrait
            setContentView(R.layout.activity_jugar);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTADOR, contPreguntasMostradas);
        outState.putInt(KEY_CONT_ACIERTO, contPreguntasAcierto);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contPreguntasMostradas = savedInstanceState.getInt(KEY_CONTADOR, 0);
        contPreguntasAcierto = savedInstanceState.getInt(KEY_CONT_ACIERTO, 0);


    }


    @Override
    protected void onStart() {
        super.onStart();

       // View frag = super.getSupportFragmentManager().findFragmentById(R.id.elFragmento).getView();

        Bundle extras = getIntent().getExtras();
        //if (frag != null) {
        /*
            radioButtonR1 = findViewById(R.id.radioButtonJugar1);
            radioButtonR2 = findViewById(R.id.radioButtonJugar2);
            radioButtonR3 = findViewById(R.id.radioButtonJugar3);
            textViewPregunta = findViewById(R.id.textViewPreguntaJugar);
            Button btnSigPregunta = findViewById(R.id.botonSigPreg);
*/
            OperacionesBD bd = new OperacionesBD(this, 1);
            if (extras != null) {
                String tema = extras.getString("TemaJugar");
                listaPreguntasTemas = bd.obtenerPreguntaTema(tema);
                respReal = cargarNuevaPregunta(listaPreguntasTemas,textViewPregunta,radioButtonR1,radioButtonR2,radioButtonR3);
            }else{ //preguntas aleatorias

                listaPreguntasTodas = bd.obtenerPreguntaRandom();
                respReal = cargarNuevaPregunta(listaPreguntasTodas,textViewPregunta,radioButtonR1,radioButtonR2,radioButtonR3);
            }

            primeraPreguntaMostrada = false;


            // y la verificacion de la pregunta y la noti
            //hacer inserciones en la bd desde un fichero de texto
            //preferencias con tema e idioma
            btnSigPregunta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (contPreguntasMostradas < 3) {
                        if (!primeraPreguntaMostrada) {
                            // Si es la primera pregunta, verifica la respuesta sin cargar una nueva pregunta

                            String respUsu = obtenerRespuestaSeleccionada(radioButtonR1, radioButtonR2, radioButtonR3);

                            if (respReal.equals(respUsu)) {
                                contPreguntasAcierto++;
                            }

                            //cargar la siguiente pregunta
                            if (extras != null) {
                                respReal = cargarNuevaPregunta(listaPreguntasTemas, textViewPregunta, radioButtonR1, radioButtonR2, radioButtonR3);
                            } else {
                                respReal = cargarNuevaPregunta(listaPreguntasTodas, textViewPregunta, radioButtonR1, radioButtonR2, radioButtonR3);
                            }

                            contPreguntasMostradas++;
                            Log.d(" primera pregunta 1", String.valueOf(contPreguntasMostradas));
                            Log.d("primera pregunta acertadas", String.valueOf(contPreguntasAcierto));
                            // Marcar que la primera pregunta ya se mostró
                            primeraPreguntaMostrada = true;
                        } else {
                            // Si no es la primera pregunta, mirar que boton se ha pulsado y cargar la sig pregunta
                            String respUsu = obtenerRespuestaSeleccionada(radioButtonR1, radioButtonR2, radioButtonR3);
                            if (respReal.equals(respUsu)) {
                                contPreguntasAcierto++;
                            }

                            //cargar la siguiente pregunta
                            if (extras != null) {
                                respReal = cargarNuevaPregunta(listaPreguntasTemas, textViewPregunta, radioButtonR1, radioButtonR2, radioButtonR3);
                            } else {
                                respReal = cargarNuevaPregunta(listaPreguntasTodas, textViewPregunta, radioButtonR1, radioButtonR2, radioButtonR3);
                            }

                            contPreguntasMostradas++;
                        }


                        Log.d("CONTADOR PREGUNTAS MOSTRADAS", String.valueOf(contPreguntasMostradas));
                        Log.d("CONTADOR PREGUNTAS acertadas", String.valueOf(contPreguntasAcierto));

                    }
                    if (contPreguntasMostradas == 3) {

                        Intent intent = new Intent(v.getContext(),FinJuegoActivity.class);
                        //Añadir al intent informacion como el nombre de la opcion seleccionada
                        intent.putExtra("pregCorrecta", String.valueOf(contPreguntasAcierto));
                        intent.putExtra("pregRespondidas", String.valueOf(contPreguntasMostradas));
                        // Inicia la actividad
                        v.getContext().startActivity(intent);
                        finish(); //finaliza esta actividad
                    }

                }
            });


        //}
    }

    // cargar pregunta da igual que sea aleatoria o de tema
    private String cargarNuevaPregunta(ArrayList<ModeloPregunta> listaPreguntas, TextView tV1, RadioButton rB1, RadioButton rB2, RadioButton rB3) {
        ModeloPregunta mPregunta = this.obtenerPreguntaLista(listaPreguntas);
        // Resto del código para configurar los elementos de la interfaz
        tV1.setText(mPregunta.getPregunta());
        rB1.setText(mPregunta.getRespuesta1());
        rB2.setText(mPregunta.getRespuesta2());
        rB3.setText(mPregunta.getRespuesta3());
        return mPregunta.getRespuestaCorrecta();
    }

    // Método para cargar una pregunta y actualizar la interfaz
    private String cargarPregunta(ModeloPregunta pregunta) {
        textViewPregunta.setText(pregunta.getPregunta());
        radioButtonR1.setText(pregunta.getRespuesta1());
        radioButtonR2.setText(pregunta.getRespuesta2());
        radioButtonR3.setText(pregunta.getRespuesta3());
        respReal = pregunta.getRespuestaCorrecta();
        return respReal;
    }

    //clase para obtener una pregunta aleatoria dada una lista cualquiera, ya sea en base a un tema o no.
    public ModeloPregunta obtenerPreguntaLista(ArrayList<ModeloPregunta> listaPreguntas) {

        Random random = new Random();
        int iAleatorio = random.nextInt(listaPreguntas.size());

        iAleatorio = random.nextInt(listaPreguntas.size());

        // Devolver la pregunta correspondiente al índice aleatorio
        // (puede repetirse una pregunta en caso de que haya pocas preguntas en la BD)

        ModeloPregunta nuevaPregunta = listaPreguntas.get(iAleatorio);

        jugarViewModel.setPregunta(nuevaPregunta); //guardarla en el ViewModel para gestionar los giros
        return nuevaPregunta;
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