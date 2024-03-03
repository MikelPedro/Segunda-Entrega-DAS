package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IntermedioJugarCrear extends AppCompatActivity {
    private String tema;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedio_jugar_crear);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           tema = extras.getString("Tema");


            TextView temaTextView = findViewById(R.id.textViewTemaInter);

            temaTextView.setText(String.format("Tema: %s", tema));
        }
    }

    public void OnClickJugar(View v){
        // aparece la pantalla de Jugar (fragment)
        Intent intentJugar = new Intent(this, JugarActivity.class);
        // Inicia la actividad
        intentJugar.putExtra("TemaJugar",tema);
        this.startActivity(intentJugar);
        finish();

    }

    public void OnClickCrearPreg(View v){

        Intent intentCP = new Intent(this, CrearPregunta.class);
        // Inicia la actividad
        intentCP.putExtra("TemaCP",tema);
        this.startActivity(intentCP);
        finish();
    }
}