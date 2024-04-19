package com.example.primera_entrega_das;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BD extends SQLiteOpenHelper {

    private static final String BD_NOMBRE = "juego.db"; //nombre de la base de datos

    private final Context context;
    //Constructor de la BD
    public BD(@Nullable Context context, int version) {
        super(context, BD_NOMBRE, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creacion de la tabla en la BD
        db.execSQL("CREATE TABLE CUESTIONARIO" +  "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Pregunta TEXT NOT NULL, " +
                "Tema TEXT NOT NULL," +
                " Respuesta1 TEXT NOT NULL," +
                " Respuesta2 TEXT NOT NULL,  " +
                "Respuesta3 TEXT NOT NULL, " +
                "Correcta TEXT NOT NULL)" );

        Log.d("BD", "Tablas creadas correctamente");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Es un metodo relacionado con mejoras necesarias en la BD.
        // y manejar la actualización de la BD cuando hay un cambio en la estructura de la misma.
        Log.d("BD", "Se está actualizando la base de datos de la versión " + oldVersion + " a la versión " + newVersion);
    }


}
