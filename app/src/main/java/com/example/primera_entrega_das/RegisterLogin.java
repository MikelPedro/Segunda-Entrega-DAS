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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

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


    public void OnClickVolver(View v){
        Intent intentVolver = new Intent(this,InicioAplicacion.class);
        this.startActivity(intentVolver);
        finish();
    }

    public void OnClickRegistro(View v){

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
                    .putString("reg","si")
                    .build();

            //One time work, llama al dowork que esta en la clase de la conexion con la BD remota
            OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionBDRemota.class)
                    .setInputData(datos)
                    .build();

            // Observador para la respuesta del trabajo
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                // Obtener el estado del trabajo
                                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                                    // Si el registro es exitoso
                                    //Mostrar dialogo para volver a la pantalla de inicio
                                    DialogFragment dialogo = new DialogoRegistro();
                                    dialogo.show(getSupportFragmentManager(), "dialogoReg");
                                } else if (workInfo.getState() == WorkInfo.State.FAILED) {
                                    // Error en el registro
                                    Toast.makeText(getApplicationContext(), "Error, ese nombre ya está registrado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

            WorkManager.getInstance(this).enqueue(otwr);



        }else{
            // Mostrar un mensaje por pantalla si uno o ambos EditText están vacíos
            Toast.makeText(this, "Por favor, completa ambos campos", Toast.LENGTH_SHORT).show();
        }
    }

}

