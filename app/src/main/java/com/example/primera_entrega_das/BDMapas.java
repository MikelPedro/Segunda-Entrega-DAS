package com.example.primera_entrega_das;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BDMapas  extends SQLiteOpenHelper {


        private static final String BD_NOMBRE = "mapa.db"; //nombre de la base de datos

        private final Context context;
        //Constructor de la BD
        public BDMapas(@Nullable Context context, int version) {
            super(context, BD_NOMBRE, null, 2);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase dbmap) {
            // Creacion de la tabla en la BD
            dbmap.execSQL("CREATE TABLE MAPAS" +  "( idM INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
            // Es un metodo relacionado con mejoras necesarias en la BD.
            // y manejar la actualización de la BD cuando hay un cambio en la estructura de la misma.
            Log.d("BD", "Se está actualizando la base de datos de la versión " + oldVersion + " a la versión " + newVersion);
        }


}