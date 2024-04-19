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

    //arraylist utilizado para que no se repitan preguntas ya mostradas
    private static ArrayList<Integer> preguntasRealizadas;


    static {
        OperacionesBD.preguntasRealizadas = new ArrayList<>();
    }

    public OperacionesBD(@Nullable Context context, int version) {
        super(context, version);
    }

    //Metodo para insertar una nueva pregunta en la base de datos desde la clase CrearPregunta
    public long insertarPregunta(String pregunta, String tema, String r1, String r2, String r3, String correcta){

        //puntero a la BD
        SQLiteDatabase db = getWritableDatabase();

        //Usare content values para la interaccion con la BD
        ContentValues nuevo = new ContentValues();

        //Insertar valor asociado al campo especificado en la tabla
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


    //metodo para obtener preguntas en base al tema que puede ser vacio, indicando de esa manera que se quiere una pregunta de cualquier tema
    private ArrayList<Integer> obtenerIdsRandom(String tema){
        ArrayList<Integer> listaIdRandom = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cu;
        if(tema.contentEquals("")) {
            //Consulta sobre todos los temas (se seleccionan todos los ids)
             cu = db.rawQuery("SELECT id FROM Cuestionario", null);
        }else{
            String[] elTema = new String[1];
            elTema[0] = tema;
            //consulta sobre un tema en concreto (se seleccionan todos los ids de un tema)
             cu = db.rawQuery("SELECT id FROM Cuestionario WHERE Tema=?", elTema);
        }

        while(cu.moveToNext()){
            int cod = cu.getInt(0); //obtener id asociado a la pregunta
            listaIdRandom.add(cod);
        }

        //cerrar cursor y bd
        cu.close();
        db.close();
        return listaIdRandom;
    }

    public int obtenerPregRandom(String tema){

        ArrayList<Integer> idValido = this.obtenerIdsRandom(tema); //se llama al metodo para obtener una lista de idValido

        Random random = new Random();

        boolean valido = false;
        int id = 0;

        //se comprueba si la pregunta ha aparecido antes, para no volver a repetirla
        while (!valido) {
            id = idValido.get(random.nextInt(idValido.size()));
            valido = !OperacionesBD.preguntasRealizadas.contains(id);

            if (!valido) {
                System.out.println("Ya se habia preguntado "+id+" . Reroll!");
                idValido.remove(id);
            }
        }

        //se añade a la lista de preguntas que ya han aparecido
        OperacionesBD.preguntasRealizadas.add(id);

        //se devuelve el id asociado a la pregunta
        return id;

    }

    //vaciar la lista en la que se han guardado las preguntas que han aparecido durante la partida
    public static void vaciarHistorial() {
        OperacionesBD.preguntasRealizadas.clear();
    }

    //obtener un objeto ModeloPregunta por medio de su id
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

        //cerrar cursor y base de datos
        cu.close();
        db.close();
        //crear pregunta con los valores obtenidos de la select
        return new ModeloPregunta(id,preg,r1,r2,r3,correcta);
    }


    // Metodo para leer y ejecutar las sentencias INSERT desde el archivo .sql guardado en la carpeta raw
    public void cargarPreguntasEnBD(Context context) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            if (context != null) {
                InputStream preguntasinputStream = context.getResources().openRawResource(R.raw.preguntas);
                BufferedReader preguntasbufferedReader = new BufferedReader(new InputStreamReader(preguntasinputStream));

                String line;
                while ((line = preguntasbufferedReader.readLine()) != null) {
                    // Ejecutar cada sentencia INSERT
                    db.execSQL(line);
                    Log.d("BD", "Sentencia SQL: " + line);
                }

                preguntasbufferedReader.close();
                preguntasinputStream.close();

            } else {
                Log.d("BD", "Context es nulo");
            }
        } catch (SQLException e) {
            Log.d("BD", "Error al ejecutar sentencia SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("BD", "Error de entrada/salida: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
