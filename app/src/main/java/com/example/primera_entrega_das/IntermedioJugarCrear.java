package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class IntermedioJugarCrear extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedio_jugar_crear);
    }

    public void OnClickJugar(View v){
        // aparece la pantalla de Jugar (fragment)

    }

    public void OnClickCrearPreg(View v){

        Intent intent = new Intent(this, CrearPregunta.class);
        // Inicia la actividad
        this.startActivity(intent);
        finish();
    }
}