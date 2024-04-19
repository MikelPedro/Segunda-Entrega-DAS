package com.example.primera_entrega_das;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.Random;

public class ConexionRemotaMapas  extends Worker {

    private static ArrayList<Integer> pregRealizadas;

    static {
        ConexionRemotaMapas.pregRealizadas = new ArrayList<>();
    }

    public ConexionRemotaMapas(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String accion = getInputData().getString("accion");


        return null;
    }


    public CiudadPregunta obtenerMapaPorId(int id){

        SQLiteDatabase db = getReadableDatabase();

        String[] elId = new String[1];
        elId[0] = Integer.toString(id);

        //Consulta a realizar en base al tema que se ha seleccionado en la app
        Cursor cu = db.rawQuery("SELECT * FROM MAPAS WHERE id=?", elId);


        cu.moveToNext();
        double lat = cu.getDouble(1);
        double longi = cu.getDouble(2);
        String r1 = cu.getString(3);
        String r2 = cu.getString(4);
        String r3 = cu.getString(5);
        String correcta = cu.getString(6);

        //cerrar cursor y base de datos
        cu.close();
        db.close();
        //crear pregunta con los valores obtenidos de la select
        return new CiudadPregunta(id,lat,longi,r1,r2,r3,correcta);
    }


    private ArrayList<Integer> obtenerIdsMapasRandom(){
        ArrayList<Integer> listaIdRandom = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cu;

        //Consulta (se seleccionan todos los ids)
        cu = db.rawQuery("SELECT id FROM MAPAS", null);

        while(cu.moveToNext()){
            int cod = cu.getInt(0); //obtener id asociado al mapa
            listaIdRandom.add(cod);
        }

        //cerrar cursor y bd
        cu.close();
        db.close();
        return listaIdRandom;
    }


    public int obtenerMapaRandom(){
        ArrayList<Integer> idValido = this.obtenerIdsMapasRandom();
        Random random = new Random();

        boolean valido = false;
        int id = 0;

        //se comprueba si la pregunta sobre el mapa ha aparecido antes, para no volver a repetirla
        while (!valido) {
            id = idValido.get(random.nextInt(idValido.size()));
            valido = !ConexionRemotaMapas.pregRealizadas.contains(id);

            if (!valido) {
                System.out.println("Ya se habia preguntado "+id+" . Reroll!");
                idValido.remove(id);
            }
        }

        //se añade a la lista de preguntas que ya han aparecido
        ConexionRemotaMapas.pregRealizadas.add(id);

        return id;
    }

    public static void vaciarHistorial() {
        ConexionRemotaMapas.pregRealizadas.clear();
    }




}
