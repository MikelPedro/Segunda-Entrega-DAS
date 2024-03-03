package com.example.primera_entrega_das;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

public class OperacionesBD extends BD{

    public OperacionesBD(@Nullable Context context, int version) {
        super(context, version);
    }

    public long insertarPregunta(String pregunta, String tema, String r1, String r2, String r3, String correcta){

        //puntero a la BD
        //BD miBD = new BD(context, 1);
        SQLiteDatabase db = getWritableDatabase();

        //Usare content values para la interaccion con la BD
        ContentValues nuevo = new ContentValues();

        nuevo.put("Pregunta", pregunta);
        nuevo.put("Tema", tema);
        nuevo.put("Respuesta1", r1);
        nuevo.put("Respuesta2", r2);
        nuevo.put("Respuesta3", r3);
        nuevo.put("Correcta", correcta);

        long id = db.insert("Cuestionario", null, nuevo);

        db.close();
        Log.d("BASE DE DATOS", "INSERCION CORRECTA EN INSERTAR PREGUNTAS");
        return id; //devolver el id que se asigna a esa insercion
    }
}
