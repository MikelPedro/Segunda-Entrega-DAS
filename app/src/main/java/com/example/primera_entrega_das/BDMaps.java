package com.example.primera_entrega_das;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BDMaps extends SQLiteOpenHelper {

    private static final String BD_NOM = "mapas.db"; //nombre de la base de datos

    public BDMaps(@Nullable Context context, int version) {
        super(context, BD_NOM, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE MAPAS" +  "( idMap INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Lat DOUBLE NOT NULL, " +
                "Longi DOUBLE NOT NULL," +
                " Respuesta1 TEXT NOT NULL," +
                " Respuesta2 TEXT NOT NULL,  " +
                "Respuesta3 TEXT NOT NULL, " +
                "Correcta TEXT NOT NULL)" );

        Log.d("BD2", "Tablas creadas correctamente");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
