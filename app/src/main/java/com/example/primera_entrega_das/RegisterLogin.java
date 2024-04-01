package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

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
        Toolbar toolbar = findViewById(R.id.labarra);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_labarra,menu); //para que aparezca el menu de la actionbar (icono, textos, boton)
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
        EditText editTextNombre = this.findViewById(R.id.editTextNombre);
        EditText editTextPass = this.findViewById(R.id.editTextTextPassword);
        String nombre = editTextNombre.getText().toString().trim(); //se convierte a string y se elimina espacios en blanco
        String contraseña = editTextPass.getText().toString().trim();

        //Comprobar que ninguno de los campos esta vacio
        if (!nombre.isEmpty() && !contraseña.isEmpty()) {
            //Mostrar dialogo para volver a la pantalla de inicio
            DialogFragment dialogo = new DialogoRegistro();
            dialogo.show(getSupportFragmentManager(), "dialogoReg");
        }else{
            // Mostrar un mensaje por pantalla si uno o ambos EditText están vacíos
            Toast.makeText(this, "Por favor, completa ambos campos", Toast.LENGTH_SHORT).show();
        }
    }

}

