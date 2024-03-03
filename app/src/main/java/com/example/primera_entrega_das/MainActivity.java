package com.example.primera_entrega_das;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.labarra);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_labarra,menu); //para el menu de la toolbar
        return true;
    }


    // Enlace con el boton de temas
    public void OnClickTemas(View v){
        // Intent desde MainAsctivity a Temas
        Intent intent = new Intent(this, Temas.class);
        //Añadir al intent informacion como el nombre de la opcion seleccionada
        //intent.putExtra("nombreOpcion", opcionSeleccionada);
        // Inicia la actividad
        this.startActivity(intent);
        //getContext().startActivity(intent);
        //finish(); si le das al triangulo no puedes volver atras
    }

    public void OnClickCrearP(View v){
        Intent intent = new Intent(this, CrearPregunta.class);
        this.startActivity(intent);
        finish();
    }

}