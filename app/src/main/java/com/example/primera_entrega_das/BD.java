package com.example.primera_entrega_das;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BD extends SQLiteOpenHelper {

    private static final String BD_NOMBRE = "juego.db";

    public BD(@Nullable Context context, int version) {
        super(context, BD_NOMBRE, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creacion de la tabla
        db.execSQL("CREATE TABLE CUESTIONARIO" +  "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Pregunta TEXT NOT NULL, " +
                "Tema TEXT NOT NULL," +
                " Respuesta1 TEXT NOT NULL," +
                " Respuesta2 TEXT NOT NULL,  " +
                "Respuesta3 TEXT NOT NULL, " +
                "Correcta TEXT NOT NULL)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Es un metodo relacionado a mejoras necesarias por cambios de version en al app
    }
}
