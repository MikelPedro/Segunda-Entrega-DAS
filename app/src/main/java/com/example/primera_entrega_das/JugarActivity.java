package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class JugarActivity extends AppCompatActivity {

    private String respReal;

    private RadioButton radioButtonR1, radioButtonR2, radioButtonR3;
    private TextView textViewPregunta;

    private Button btnSigPregunta;

    private ModeloPregunta mPregunta;

    private ImageView imgJugar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_jugar);

        //Obtener la informacion guardada en el intent
        super.getIntent().putExtra("idPregunta", super.getIntent().getExtras().getInt("idPregunta"));
        super.getIntent().putExtra("TemaJugar", super.getIntent().getExtras().getString("TemaJugar"));
        super.getIntent().putExtra("pregCorrecta",super.getIntent().getExtras().getInt("pregCorrecta"));
        super.getIntent().putExtra("pregRespondida",super.getIntent().getExtras().getInt("pregRespondida"));
        //para añadir la imagen a la pantalla de jugar
        super.getIntent().putExtra("imgJugarTema",super.getIntent().getExtras().getInt("imgJugarTema"));

        //encontrar elementos del view
        imgJugar = findViewById(R.id.imgJugarTema);
        radioButtonR1 = findViewById(R.id.radioButtonJugar1);
        radioButtonR2 = findViewById(R.id.radioButtonJugar2);
        radioButtonR3 = findViewById(R.id.radioButtonJugar3);
        textViewPregunta = findViewById(R.id.textViewPreguntaJugar);
        btnSigPregunta = findViewById(R.id.botonSigPreg);

        OperacionesBD bd = new OperacionesBD(this, 1);

        //obtener un objeto pregunta en base al id que se obtiene del intent
        mPregunta = bd.obtenerPregPorId(super.getIntent().getExtras().getInt("idPregunta"));

        //devolver resultado correcto de la pregunta y añadir a los elementos la informacion (pregunta y respuestas) que contiene la pregunta
        respReal = cargarPregunta(mPregunta,super.getIntent().getExtras().getInt("imgJugarTema"));

    }


    @Override
    protected void onStart() {
        super.onStart();

        Bundle extras = getIntent().getExtras();
        OperacionesBD bd = new OperacionesBD(this, 1);
        //cargar info de la pregunta en el view
        cargarPregunta(bd.obtenerPregPorId(extras.getInt("idPregunta")),extras.getInt("imgJugarTema"));

        //Listener a boton de siguiente pregunta
        btnSigPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Obtener la respuesta del usuario y guardarla en una variable
                String respUsu = obtenerRespuestaSeleccionada(radioButtonR1, radioButtonR2, radioButtonR3);

                //Comparar la respuesta real con la del usuario
                if (respReal.equals(respUsu)) {
                    //sumar 1 punto al contador de preguntas correctas
                    JugarActivity.super.getIntent().putExtra("pregCorrecta",JugarActivity.super.getIntent().getExtras().getInt("pregCorrecta")+1);
                }

                //sumar 1 punto al contador de preguntas respondidas
                JugarActivity.super.getIntent().putExtra("pregRespondida",JugarActivity.super.getIntent().getExtras().getInt("pregRespondida")+1);

                Log.d("PREGUNTAS RESPONDIDAS", String.valueOf(JugarActivity.super.getIntent().getExtras().getInt("pregRespondida")));

                if (JugarActivity.super.getIntent().getExtras().getInt("pregRespondida") == 3) {

                    //si es igual a 3 se acaba el juego y se muestra una nueva actividad con el resultado
                    Intent intent = new Intent(JugarActivity.this,FinJuegoActivity.class);
                    //Añadir al intent informacion sobre las preguntas acertadas y respondidas
                    intent.putExtra("pregCorrecta", JugarActivity.super.getIntent().getExtras().getInt("pregCorrecta"));
                    intent.putExtra("pregRespondida", JugarActivity.super.getIntent().getExtras().getInt("pregRespondida"));

                    // Limpia el historial de preguntas que han aparecido en la partida
                    OperacionesBD.vaciarHistorial();

                    JugarActivity.super.startActivity(intent);
                    finish(); //finaliza esta actividad
                }else{

                    //Recargar la actividad si todavia no han aparecido 3 preguntas
                    Intent intent = new Intent(JugarActivity.this, JugarActivity.class);
                    //Guardar en el intent toda la informacion necesaria
                    //Obtener otra pregunta en base al tema o de manera random
                    intent.putExtra("idPregunta",bd.obtenerPregRandom(JugarActivity.super.getIntent().getExtras().getString("TemaJugar")));
                    intent.putExtra("TemaJugar",JugarActivity.super.getIntent().getExtras().getString("TemaJugar"));
                    intent.putExtra("pregCorrecta", JugarActivity.super.getIntent().getExtras().getInt("pregCorrecta"));
                    intent.putExtra("pregRespondida", JugarActivity.super.getIntent().getExtras().getInt("pregRespondida"));
                    intent.putExtra("imgJugarTema", JugarActivity.super.getIntent().getExtras().getInt("imgJugarTema"));
                    JugarActivity.super.startActivity(intent);
                    finish();
                }

            }
        });
    }

    // Método para cargar una pregunta y actualizar la interfaz
    private String cargarPregunta(ModeloPregunta pregunta, int imagen) {
        textViewPregunta.setText(pregunta.getPregunta());
        radioButtonR1.setText(pregunta.getRespuesta1());
        radioButtonR2.setText(pregunta.getRespuesta2());
        radioButtonR3.setText(pregunta.getRespuesta3());
        imgJugar.setImageResource(imagen);
        respReal = pregunta.getRespuestaCorrecta();
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