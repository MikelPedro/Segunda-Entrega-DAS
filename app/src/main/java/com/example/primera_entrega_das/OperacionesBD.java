package com.example.primera_entrega_das;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class OperacionesBD extends BD{

    private static ArrayList<Integer> preguntasRealizadas;

    static {
        OperacionesBD.preguntasRealizadas = new ArrayList<>();
    }

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


    private ArrayList<Integer> obtenerIdsRandom(String tema){
        ArrayList<Integer> listaIdRandom = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cu;
        if(tema.contentEquals("")) {
            //Consulta sobre todos los temas
             cu = db.rawQuery("SELECT id FROM Cuestionario", null);
        }else{
            String[] elTema = new String[1];
            elTema[0] = tema;

             cu = db.rawQuery("SELECT id FROM Cuestionario WHERE Tema=?", elTema);
        }

        while(cu.moveToNext()){
            int cod = cu.getInt(0);
            listaIdRandom.add(cod);
        }

        cu.close();
        return listaIdRandom;
    }

    public int obtenerPregRandom(String tema){

        ArrayList<Integer> idValido = this.obtenerIdsRandom(tema);

        Random random = new Random();

        boolean valido = false;
        int id = 0;

        while (!valido) {
            id = idValido.get(random.nextInt(idValido.size()));
            valido = !OperacionesBD.preguntasRealizadas.contains(id);

            if (!valido) {
                System.out.println("Ya se habia preguntado "+id+" . Reroll!");
                idValido.remove(id);
            }
        }

        OperacionesBD.preguntasRealizadas.add(id);

        return id;

    }

    public static void vaciarHistorial() {
        OperacionesBD.preguntasRealizadas.clear();
    }

    public ModeloPregunta obtenerPregPorId(int id){

        SQLiteDatabase db = getReadableDatabase();

        String[] elId = new String[1];
        elId[0] = Integer.toString(id);

        //Consulta a realizar en base al tema que se ha seleccionado en la app
        Cursor cu = db.rawQuery("SELECT * FROM Cuestionario WHERE id=?", elId);


        cu.moveToNext();
        String preg = cu.getString(1);
        String r1 = cu.getString(3);
        String r2 = cu.getString(4);
        String r3 = cu.getString(5);
        String correcta = cu.getString(6);

        //cerrar cursor
        cu.close();
        //db.close();
        //crear pregunta con los valores obtenidos de la select
        return new ModeloPregunta(id,preg,r1,r2,r3,correcta);
    }


    public void cargarPreguntasEnBD(Context context) {
        // Leer y ejecutar las sentencias INSERT desde el archivo (chatgpt)
        SQLiteDatabase db = getWritableDatabase();
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
