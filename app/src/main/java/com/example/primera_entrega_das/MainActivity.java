package com.example.primera_entrega_das;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
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

    public void OnClickIconoPregunta(MenuItem item){
        //Mostrar dialogo
        DialogFragment dialogo = new DialogoWiki();
        dialogo.show(getSupportFragmentManager(), "dialogoIcono");
    }

    /*
    public void OnClickConfig(MenuItem item){
        Intent intent = new Intent(this, Configuracion.class);
        startActivity(intent);
    }
    */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        if (key.equals("language_preference") || key.equals("theme_preference")) {
            recreate(); // Vuelve a crear la actividad para aplicar los cambios
        }
    }
}