package com.example.primera_entrega_das;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;
import androidx.work.WorkRequest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

import kotlinx.coroutines.flow.Flow;

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
        OperacionesBDMapas bdm = new OperacionesBDMapas(this, 1);
        // Llamada al método cargarPreguntasEnBD para cargar todas las preguntas de un archivo en la BD
        bdm.vaciarTablaPreguntas();
        bdm.cargarPreguntasEnBDMapas(this);
        bd.cargarPreguntasEnBD(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)!=
                    PackageManager.PERMISSION_GRANTED) {
                //PEDIR EL PERMISO
                ActivityCompat.requestPermissions(this, new
                        String[]{Manifest.permission.POST_NOTIFICATIONS}, 11);
            }
        }
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

    public void OnClickIconoMensaje(MenuItem item){
        //Mostrar dialogo con 2 opciones (ver enlace y volver)
        DialogFragment dialogo = new DialogoFB();
        dialogo.show(getSupportFragmentManager(), "dialogofb");
    }

    public void OnClickIconoPerfil(MenuItem item){
        //Al darle click te lleva a la pagina del perfil del usuario
        Intent perfil = new Intent(this,PerfilUsuario.class);
        perfil.putExtra("nombreUsu",nombreUsu);
        this.startActivity(perfil);
    }

    public void OnClickMapas(View v){

        Log.d("MAIN","ENTRA");
        OperacionesBDMapas opBDMap = new OperacionesBDMapas(this,1);

        Intent mapas = new Intent(this, MapsJuegoActivity.class);
        Log.d("MAIN", String.valueOf(opBDMap.obtenerPregRandom()));
        mapas.putExtra("idPregunta", opBDMap.obtenerPregRandom());
        mapas.putExtra("pregCorrecta", 0);
        mapas.putExtra("pregRespondida", 0);
        this.startActivity(mapas);
        finish();





    }

}