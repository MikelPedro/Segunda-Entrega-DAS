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

    private ModeloPregunta mPregunta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_jugar);

        //tiene que llamarse igual
        super.getIntent().putExtra("idPregunta", super.getIntent().getExtras().getInt("idPregunta"));
        super.getIntent().putExtra("TemaJugar", super.getIntent().getExtras().getString("TemaJugar"));
        super.getIntent().putExtra("pregCorrecta",super.getIntent().getExtras().getInt("pregCorrecta"));
        super.getIntent().putExtra("pregRespondida",super.getIntent().getExtras().getInt("pregRespondida"));
        //jugarViewModel = new ViewModelProvider(this).get(JugarViewModel.class); //chatgpt


        radioButtonR1 = findViewById(R.id.radioButtonJugar1);
        radioButtonR2 = findViewById(R.id.radioButtonJugar2);
        radioButtonR3 = findViewById(R.id.radioButtonJugar3);
        textViewPregunta = findViewById(R.id.textViewPreguntaJugar);
        btnSigPregunta = findViewById(R.id.botonSigPreg);

        OperacionesBD bd = new OperacionesBD(this, 1);

        mPregunta = bd.obtenerPregPorId(super.getIntent().getExtras().getInt("idPregunta"));

        respReal = cargarPregunta(mPregunta);


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

        Bundle extras = getIntent().getExtras();


            OperacionesBD bd = new OperacionesBD(this, 1);

            cargarPregunta(bd.obtenerPregPorId(extras.getInt("idPregunta")));

            btnSigPregunta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    // Si es la primera pregunta, verifica la respuesta sin cargar una nueva pregunta

                    String respUsu = obtenerRespuestaSeleccionada(radioButtonR1, radioButtonR2, radioButtonR3);

                    if (respReal.equals(respUsu)) {
                        JugarActivity.super.getIntent().putExtra("pregCorrecta",JugarActivity.super.getIntent().getExtras().getInt("pregCorrecta")+1);

                    }

                    JugarActivity.super.getIntent().putExtra("pregRespondida",JugarActivity.super.getIntent().getExtras().getInt("pregRespondida")+1);

                    Log.d("PREGUNTAS RESPONDIDAS", String.valueOf(JugarActivity.super.getIntent().getExtras().getInt("pregRespondida")));

                    if (JugarActivity.super.getIntent().getExtras().getInt("pregRespondida") == 3) {

                        Intent intent = new Intent(JugarActivity.this,FinJuegoActivity.class);
                        //Añadir al intent informacion como el nombre de la opcion seleccionada
                        intent.putExtra("pregCorrecta", JugarActivity.super.getIntent().getExtras().getInt("pregCorrecta"));
                        intent.putExtra("pregRespondida", JugarActivity.super.getIntent().getExtras().getInt("pregRespondida"));
                        // Inicia la actividad
                        JugarActivity.super.startActivity(intent);
                        finish(); //finaliza esta actividad
                    }else{
                        Intent intent = new Intent(JugarActivity.this, JugarActivity.class);
                        intent.putExtra("idPregunta",bd.obtenerPregRandom(JugarActivity.super.getIntent().getExtras().getString("TemaJugar")));
                        intent.putExtra("TemaJugar",JugarActivity.super.getIntent().getExtras().getString("TemaJugar"));
                        intent.putExtra("pregCorrecta", JugarActivity.super.getIntent().getExtras().getInt("pregCorrecta"));
                        intent.putExtra("pregRespondida", JugarActivity.super.getIntent().getExtras().getInt("pregRespondida"));
                        JugarActivity.super.startActivity(intent);
                        finish();
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