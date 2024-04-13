package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class PerfilUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
    }


    public void OnClickVolverMain(View v){
        //Al darle click te lleva a la MainActivity
        Intent volver = new Intent(this,MainActivity.class);
        this.startActivity(volver);
        finish();
    }


}