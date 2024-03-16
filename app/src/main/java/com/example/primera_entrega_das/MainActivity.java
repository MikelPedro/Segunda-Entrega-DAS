package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Encontrar la toolbar que he creado
        Toolbar toolbar = findViewById(R.id.labarra);
        setSupportActionBar(toolbar);

        OperacionesBD bd = new OperacionesBD(this, 1);
        // Llamada al método cargarPreguntasEnBD para cargar todas las preguntas de un archivo en la BD
        bd.cargarPreguntasEnBD(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_labarra,menu); //para que aparezca el menu de la toolbar
        return true;
    }

    // Enlace con el boton de temas
    public void OnClickTemas(View v){
        Intent intent = new Intent(this, Temas.class);
        //Añadir al intent informacion como el nombre de la opcion seleccionada
        // Inicia la actividad
        this.startActivity(intent);
        //si le das al triangulo te saca de la app
        finish();

    }

    public void OnClickJugar(View v){
        //opcion en la cual al hacer click juegas una partida en la que apareceran 3 preguntas
        // de todos los temas que hay registrados
        Intent intentJugar = new Intent(this, JugarActivity.class);
        OperacionesBD opBD = new OperacionesBD(this,1);

        int idP = opBD.obtenerPregRandom(""); //para obtener preguntas de todos los temas pasa por parametro ""
        //se devuelve el id de una pregunta random
        //se guarda en el intent los parametros necesarios
        intentJugar.putExtra("idPregunta",idP);
        intentJugar.putExtra("TemaJugar","");
        intentJugar.putExtra("pregCorrecta",0);
        intentJugar.putExtra("pregRespondida",0);
        intentJugar.putExtra("imgJugarTema",R.drawable.img_jugar_tema); //icono para preguntas aleatorias
        this.startActivity(intentJugar);
        finish();
    }

    public void OnClickIconoPregunta(MenuItem item){
        //Mostrar dialogo con 2 opciones
        DialogFragment dialogo = new DialogoWiki();
        dialogo.show(getSupportFragmentManager(), "dialogoIcono");
    }

}