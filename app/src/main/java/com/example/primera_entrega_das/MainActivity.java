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

        Toolbar toolbar = findViewById(R.id.labarra);
        setSupportActionBar(toolbar);

        OperacionesBD bd = new OperacionesBD(this, 1);
        // Llamada al método cargarPreguntasEnBD para cargar preguntas en la BD
        bd.cargarPreguntasEnBD(this);

        //TODO:
        // Idiomas
        // Estilo (preferencia)
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
        finish(); //si le das al triangulo no puedes volver atras

    }

    public void OnClickJugar(View v){
        Intent intentJugar = new Intent(this, JugarActivity.class);
        OperacionesBD opBD = new OperacionesBD(this,1);

        int idP = opBD.obtenerPregRandom("");
        intentJugar.putExtra("idPregunta",idP);
        intentJugar.putExtra("TemaJugar",""); // por si acaso puee dar problema
        intentJugar.putExtra("pregCorrecta",0);
        intentJugar.putExtra("pregRespondida",0);
        this.startActivity(intentJugar);
        finish();
    }

    public void OnClickIconoPregunta(MenuItem item){
        //Mostrar dialogo
        DialogFragment dialogo = new DialogoWiki();
        dialogo.show(getSupportFragmentManager(), "dialogoIcono");
    }


}