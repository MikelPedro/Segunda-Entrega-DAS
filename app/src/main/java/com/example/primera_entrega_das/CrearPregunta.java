package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class CrearPregunta extends AppCompatActivity {

    private EditText editTextPregunta, editTextRespuesta1, editTextRespuesta2, editTextRespuesta3;
    private RadioButton radioButtonRespuesta1, radioButtonRespuesta2, radioButtonRespuesta3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pregunta);

        editTextPregunta = findViewById(R.id.laPregunta);
        editTextRespuesta1 = findViewById(R.id.editTextRespuesta1);
        editTextRespuesta2 = findViewById(R.id.editTextRespuesta2);
        editTextRespuesta3 = findViewById(R.id.editTextRespuesta3);

        radioButtonRespuesta1 = findViewById(R.id.radioButtonRespuesta1);
        radioButtonRespuesta2 = findViewById(R.id.radioButtonRespuesta2);
        radioButtonRespuesta3 = findViewById(R.id.radioButtonRespuesta3);

    }

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
      // guardar pregunta en la BD y que aparezca dialogo o noti de que se ha guardado correctamente

        // Obtener la pregunta y las respuestas ingresadas por el usuario
        String pregunta = editTextPregunta.getText().toString();
        String respuesta1 = editTextRespuesta1.getText().toString();
        String respuesta2 = editTextRespuesta2.getText().toString();
        String respuesta3 = editTextRespuesta3.getText().toString();

        // Obtener la respuesta correcta seleccionada
        String respuestaCorrecta = obtenerRespuestaCorrecta();

        //Guardar en la BD:


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