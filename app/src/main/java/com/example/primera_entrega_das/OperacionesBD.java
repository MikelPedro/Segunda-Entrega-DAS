package com.example.primera_entrega_das;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class OperacionesBD extends BD{

    Context context;
    public OperacionesBD(@Nullable Context context, int version) {
        super(context, version);
    }

    public long insertarPregunta(String pregunta, String tema, String r1, String r2, String r3, String correcta){

        //puntero a la BD
        BD miBD = new BD(context, 1);
        SQLiteDatabase db = miBD.getWritableDatabase();

        //Usare content values para la interaccion con la BD
        ContentValues nuevo = new ContentValues();

        nuevo.put("Pregunta", pregunta);
        nuevo.put("Tema", tema);
        nuevo.put("Respuesta1", r1);
        nuevo.put("Respuesta1", r2);
        nuevo.put("Respuesta1", r3);
        nuevo.put("Correcta", correcta);

        long id = db.insert("Cuestionario", null, nuevo);

        db.close();
        System.out.println("HA HECHO UNA INSERCION");
        return id; //devolver el id que se asigna a esa insercion
    }
}
