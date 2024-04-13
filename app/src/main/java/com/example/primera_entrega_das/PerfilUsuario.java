package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PerfilUsuario extends AppCompatActivity {

    private String nomUsuario = "";
    private ImageView imgPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        TextView tvNombre = findViewById(R.id.textViewNombre);
        imgPerfil = findViewById(R.id.imagePerfil);

        //Traer por medio del intent, el nombre de usuario y sus puntos
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            nomUsuario = extras.getString("nombreUsu");
            tvNombre.setText("Nombre: " + nomUsuario);
        }


        //cargar la foto (si tiene)


    }


    public void OnClickVolverMain(View v){
        //Al darle click te lleva a la MainActivity
        Intent volver = new Intent(this,MainActivity.class);
        this.startActivity(volver);
        finish();
    }


}