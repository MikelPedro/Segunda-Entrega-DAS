package com.example.primera_entrega_das;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.labarra);
        setSupportActionBar(toolbar);

        OperacionesBD bd = new OperacionesBD(this, 1);
        SQLiteDatabase db = bd.getWritableDatabase();

        // Llamada al método cargarPreguntasEnBD para cargar preguntas en la BD
        bd.cargarPreguntasEnBD(db, this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //TODO: Pantalla horizontal en fin juego activity
        // Fragment (no perder info)
        // Idiomas
        // Estilo (preferencia)
        // intent implicito
        // docu
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

    public void OnClickJugar(View v){
        Intent intentJugar = new Intent(this, JugarActivity.class);
        this.startActivity(intentJugar);
        //finsih();
    }

}