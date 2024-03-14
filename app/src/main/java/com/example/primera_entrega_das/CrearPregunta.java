package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class CrearPregunta extends AppCompatActivity {

    private EditText editTextPregunta, editTextRespuesta1, editTextRespuesta2, editTextRespuesta3;
    private RadioButton radioButtonRespuesta1, radioButtonRespuesta2, radioButtonRespuesta3;
    private String tema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pregunta);

        //Encontrar todos los editTexts y radioButtons
        editTextPregunta = findViewById(R.id.laPregunta);
        editTextRespuesta1 = findViewById(R.id.editTextRespuesta1);
        editTextRespuesta2 = findViewById(R.id.editTextRespuesta2);
        editTextRespuesta3 = findViewById(R.id.editTextRespuesta3);

        radioButtonRespuesta1 = findViewById(R.id.radioButtonRespuesta1);
        radioButtonRespuesta2 = findViewById(R.id.radioButtonRespuesta2);
        radioButtonRespuesta3 = findViewById(R.id.radioButtonRespuesta3);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tema = extras.getString("TemaCP"); //coger tema que hemos guardado en el intent

            TextView temaTextView = findViewById(R.id.textTema);

            temaTextView.setText(String.format("Tema: %s", tema));
        }

    }

    //Encontrar el radioButton que esta marcado por el usuario, para indicar la respuesta correcta
    private String obtenerRespuestaCorrecta() {
        if (radioButtonRespuesta1.isChecked()) {
            return editTextRespuesta1.getText().toString();
        } else if (radioButtonRespuesta2.isChecked()) {
            return editTextRespuesta2.getText().toString();
        } else if (radioButtonRespuesta3.isChecked()) {
            return editTextRespuesta3.getText().toString();
        }
        return "";
    }

    public void OnClickPregunta(View v){

        // Guardar pregunta en la BD y que aparezca dialogo de que se ha guardado correctamente

        // Obtener la pregunta y las respuestas ingresadas por el usuario
        String pregunta = editTextPregunta.getText().toString();
        String respuesta1 = editTextRespuesta1.getText().toString();
        String respuesta2 = editTextRespuesta2.getText().toString();
        String respuesta3 = editTextRespuesta3.getText().toString();

        // Obtener la respuesta correcta seleccionada
        String respuestaCorrecta = obtenerRespuestaCorrecta();


        //llamada a la clase que contiene el metodo en la BD para guardar la pregunta
        OperacionesBD bd = new OperacionesBD(this,1);
        long id = bd.insertarPregunta(pregunta,tema,respuesta1,respuesta2,respuesta3,respuestaCorrecta);
        Log.d("id en la BD",String.valueOf(id));

        // Limpiar y restaurar los valores por defecto de los EditText
        editTextPregunta.setText("");
        editTextRespuesta1.setText("");
        editTextRespuesta1.setHint("Respuesta 1");

        editTextRespuesta2.setText("");
        editTextRespuesta2.setHint("Respuesta 2");

        editTextRespuesta3.setText("");
        editTextRespuesta3.setHint("Respuesta 3");

        // Limpiar selección de los RadioButtons
        radioButtonRespuesta1.setChecked(false);
        radioButtonRespuesta2.setChecked(false);
        radioButtonRespuesta3.setChecked(false);

        //Mostrar dialogo
        DialogFragment dialogo = new DialogoCP();
        dialogo.show(getSupportFragmentManager(), "etiqueta");

    }

    public void OnClickVolver(View v){

        //Intent para volver al menu principal
        Intent intent = new Intent(this, MainActivity.class);
        // Inicia la actividad
        this.startActivity(intent);
        finish();
    }

}