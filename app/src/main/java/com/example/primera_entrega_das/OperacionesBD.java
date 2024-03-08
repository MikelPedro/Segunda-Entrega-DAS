package com.example.primera_entrega_das;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Display;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

    public ArrayList<ModeloPregunta> obtenerPreguntaRandom(){

        ArrayList<ModeloPregunta> listaPreguntasRandom = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        //Consulta sobre todos los temas
        Cursor cu = db.rawQuery("SELECT * FROM Cuestionario", null);

        while (cu.moveToNext()){
            int cod = cu.getInt(0);
            String pregunta = cu.getString(1);
            String r1 = cu.getString(3); // ya que no queremos el atributo de temas pasamos al 3
            String r2 = cu.getString(4);
            String r3 = cu.getString(5);
            String correcta = cu.getString(6);

            // Crear el objeto de ModeloPregunta con los datos obtenidos y añadirlo a una ArrayList
            ModeloPregunta nuevaPregunta = new ModeloPregunta(cod, pregunta, r1, r2, r3, correcta);
            listaPreguntasRandom.add(nuevaPregunta);
        }

        // Cerrar las conexiones con el cursor y BD
        cu.close();
        db.close();

        return listaPreguntasRandom;
    }

    //Metodo para obtener las preguntas relacionadas con el tema que ha escogido el usuario
    public ArrayList<ModeloPregunta> obtenerPreguntaTema(String tema){

        ArrayList<ModeloPregunta> listaPreguntasTema = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] elTema = new String[1];
        elTema[0] = tema;

        //Consulta a realizar en base al tema que se ha seleccionado en la app
        Cursor cu = db.rawQuery("SELECT * FROM Cuestionario WHERE Tema=?", elTema);

        //obtener los datos de la consulta
        while (cu.moveToNext()){
            int cod = cu.getInt(0);
            String pregunta = cu.getString(1);
            String r1 = cu.getString(3);
            String r2 = cu.getString(4);
            String r3 = cu.getString(5);
            String correcta = cu.getString(6);

            // Crear el objeto de ModeloPregunta con los datos obtenidos y añadirlo a una ArrayList
            ModeloPregunta nuevaPregunta = new ModeloPregunta(cod, pregunta, r1, r2, r3, correcta);
            listaPreguntasTema.add(nuevaPregunta);
        }

        // Cerrar las conexiones con el cursor y BD
        cu.close();
        db.close();

        return listaPreguntasTema;
    }

    public void cargarPreguntasEnBD(SQLiteDatabase db,Context context) {
        // Leer y ejecutar las sentencias INSERT desde el archivo
        try {
            if (context != null) {
                InputStream inputStream = context.getResources().openRawResource(R.raw.preguntas);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    // Ejecutar cada sentencia INSERT
                    db.execSQL(line);
                    Log.d("BD", "Sentencia SQL: " + line);
                }

                bufferedReader.close();
                inputStream.close();
            } else {
                // Handle the case where context is null
                Log.e("BD", "Context es nulo");
            }
        } catch (SQLException e) {
            Log.e("BD", "Error al ejecutar sentencia SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("BD", "Error de entrada/salida: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
