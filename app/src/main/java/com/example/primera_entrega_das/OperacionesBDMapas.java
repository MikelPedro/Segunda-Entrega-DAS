package com.example.primera_entrega_das;

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

public class OperacionesBDMapas extends BDMaps{


    public OperacionesBDMapas(@Nullable Context context, int version) {
        super(context, version);
    }

    private ArrayList<Integer> obtenerIdsMapsRandom(){
        ArrayList<Integer> listaIdRandom = new ArrayList<>();

        SQLiteDatabase dbmap = getReadableDatabase();
        Cursor cu1;

        //Consulta sobre todos los temas (se seleccionan todos los ids)
        cu1 = dbmap.rawQuery("SELECT idMap FROM MAPAS", null);
        Log.d("BD", "donde hay problema");

        while(cu1.moveToNext()){
            int cod = cu1.getInt(0); //obtener id asociado a la pregunta
            listaIdRandom.add(cod);
        }

        //cerrar cursor y bd
        cu1.close();
        dbmap.close();
        return listaIdRandom;
    }

    public int obtenerPregRandom(){

        ArrayList<Integer> idValido = this.obtenerIdsMapsRandom(); //se llama al metodo para obtener una lista de idValido
        Log.d("MAIN",String.valueOf(idValido));
        Random random = new Random();

        int id = 0;
        id = idValido.get(random.nextInt(idValido.size()));
        //se devuelve el id asociado a la pregunta
        return id;

    }

    public CiudadPregunta obtenerPregPorId(int id){

        SQLiteDatabase dbmap = getReadableDatabase();

        String[] elId = new String[1];
        elId[0] = Integer.toString(id);

        //Consulta a realizar en base al tema que se ha seleccionado en la app
        Cursor cu1 = dbmap.rawQuery("SELECT * FROM MAPAS WHERE idMap=?", elId);


        cu1.moveToNext();
        Double lat = cu1.getDouble(1);
        Double longi = cu1.getDouble(2);
        String r1 = cu1.getString(3);
        String r2 = cu1.getString(4);
        String r3 = cu1.getString(5);
        String correcta = cu1.getString(6);

        //cerrar cursor y base de datos
        cu1.close();
        dbmap.close();
        //crear pregunta con los valores obtenidos de la select
        return new CiudadPregunta(id,lat,longi,r1,r2,r3,correcta);
    }

    public void cargarPreguntasEnBDMapas(Context context) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            if (context != null) {
                InputStream mapasinputStream = context.getResources().openRawResource(R.raw.mapaspreg);
                BufferedReader mapasbufferedReader = new BufferedReader(new InputStreamReader(mapasinputStream));

                String line;
                while ((line = mapasbufferedReader.readLine()) != null) {
                    // Ejecutar cada sentencia INSERT
                    db.execSQL(line);
                    Log.d("BD2", "Sentencia SQL: " + line);
                }

                mapasbufferedReader.close();
                mapasinputStream.close();

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

    public void vaciarTablaPreguntas() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MAPAS", null, null);
        db.close();
    }

}


