package com.example.primera_entrega_das;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity{

    private String nombreUsu = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Encontrar la actionbar que he creado
        Toolbar toolbar = findViewById(R.id.labarra);
        setSupportActionBar(toolbar);

        OperacionesBD bd = new OperacionesBD(this, 1);
        // Llamada al método cargarPreguntasEnBD para cargar todas las preguntas de un archivo en la BD
        bd.cargarPreguntasEnBD(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
                        Toast.makeText(MainActivity.this, " Tu token es: " + token, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_labarra,menu); //para que aparezca el menu de la actionbar (icono, textos, boton)
        return true;
    }

    // Enlace con el boton de temas
    public void OnClickTemas(View v){
        Intent intent = new Intent(this, Temas.class);
        //Añadir al intent informacion como el nombre de la opcion seleccionada
        // Inicia la actividad
        this.startActivity(intent);
        //si le das al triangulo te saca de la aplicación
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
        //si le das al triangulo te saca de la aplicación
        finish();
    }

    public void OnClickIconoPregunta(MenuItem item){
        //Mostrar dialogo con 2 opciones (ver enlace y volver)
        DialogFragment dialogo = new DialogoWiki();
        dialogo.show(getSupportFragmentManager(), "dialogoIcono");
    }

    public void OnClickIconoPerfil(MenuItem item){
        //Al darle click te lleva a la pagina del perfil del usuario
        Intent perfil = new Intent(this,PerfilUsuario.class);
        perfil.putExtra("nombreUsu",nombreUsu);
        this.startActivity(perfil);
    }


    //lo de login (quitar mas adelante)
    public void OnClickPruebas(View v){
        Intent i = new Intent(this,InicioAplicacion.class);
        this.startActivity(i);
    }
}