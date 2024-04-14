package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class InicioAplicacion extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_aplicacion);

        //Encontrar la actionbar que he creado
        Toolbar toolbar = findViewById(R.id.labarraInicio);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_labarrainicio,menu); //para que aparezca el menu de la actionbar (icono, textos, boton)
        return true;
    }

    public void OnClickIconoPregunta(MenuItem item){
        //Mostrar dialogo con 2 opciones (ver enlace y volver)
        DialogFragment dialogo = new DialogoWiki();
        dialogo.show(getSupportFragmentManager(), "dialogoIcono");
    }

    public void OnClickLogin(View v){

        //Obtener los editTexts y su contenido
        EditText editTextNombre = this.findViewById(R.id.editTextNom);
        EditText editTextPass = this.findViewById(R.id.editTextTextPass);
        String nombre = editTextNombre.getText().toString().trim(); //se convierte a string y se elimina espacios en blanco
        String contraseña = editTextPass.getText().toString().trim();

        //Comprobar que ninguno de los campos esta vacio
        if (!nombre.isEmpty() && !contraseña.isEmpty()) {

            //Para mandar los datos a la bd
            Data datos = new Data.Builder()
                    .putString("nom",nombre)
                    .putString("cont",contraseña)
                    .putString("reg","no")
                    .build();

            OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(conexionBDRemota.class)
                    .setInputData(datos)
                    .build();
            Log.d("LOGIN", "llega antes del workmanager" + nombre + " " + contraseña);
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if(workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED){
                                Log.d("LOGIN", "despues del workmanager");
                                // Iniciar la actividad principal (MainActivity) si el result es success
                                Intent main = new Intent(InicioAplicacion.this, MainActivity.class);
                                main.putExtra("nombreUsu", nombre); // Se pasa el nombre de usuario
                                startActivity(main);
                                finish(); // Cerrar la actividad de inicio de sesión
                            }
                        }
                    });
            WorkManager.getInstance(this).enqueue(otwr);

        }else{
            // Mostrar un mensaje por pantalla si uno o ambos EditText están vacíos
            Toast.makeText(this, "Por favor, completa ambos campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClickRegistrarse(View v){
        Intent intentReg = new Intent(this,RegisterLogin.class);
        this.startActivity(intentReg);
        finish();
    }
}