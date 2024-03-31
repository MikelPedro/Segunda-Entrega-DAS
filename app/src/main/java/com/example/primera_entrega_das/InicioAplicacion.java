package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class InicioAplicacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_aplicacion);

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

    public void OnClickLogin(View v){

    }

    public void OnClickRegistrarse(View v){
        Intent intentReg = new Intent(this,RegisterLogin.class);
        this.startActivity(intentReg);
        finish();
    }
}