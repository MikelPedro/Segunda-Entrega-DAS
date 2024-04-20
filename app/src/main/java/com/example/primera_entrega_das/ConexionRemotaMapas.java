package com.example.primera_entrega_das;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class ConexionRemotaMapas  extends Worker {

    int pid = 0;

    public ConexionRemotaMapas(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String accion = getInputData().getString("accion");

        if(accion.equals("obtId")){
            return obtenerIdMapasRandom();
        }else if(accion.equals("obtmapa")){
            int id = getInputData().getInt("id",pid);
            Log.d("MAIN",String.valueOf(id));
            return obtenerMapaPorId(id);
        }else{
            return null;
        }
    }


    public Result obtenerMapaPorId(int id){

        String direccion = "http://35.230.19.155:81/mapas.php?";

        HttpURLConnection urlConnection;

        //Construir URI con los parametros
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("idMapa", String.valueOf(id));
        String parametrosURL = builder.build().getEncodedQuery();

        try {

            URL destino = new URL(direccion + parametrosURL);
            Log.d("MAPA", "URI: " + destino);

            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200){

                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line, result="";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                inputStream.close();
                Log.d("RESULT", result);


                JSONObject json = new JSONObject(result);
                Log.d("RESULT", "llega aqui");

                // Convertir el JSONObject a una cadena JSON
                String ciudadJson = json.toString();
                Log.d("RESULT", ciudadJson);

                // Empaquetar la cadena JSON en un objeto Data
                Data datosMapa = new Data.Builder()
                        .putString("ciudadJson", ciudadJson)
                        .build();

                return Result.success(datosMapa);

            } else {
                return Result.failure();
            }

        }catch (Exception e){
            Log.d("MAPA","excepcion lanzada");
            return Result.failure();
        }

    }


    public Result obtenerIdMapasRandom(){
        ArrayList<Integer> listaIdRandom = new ArrayList<>();

        String direccion = "http://35.230.19.155:81/mapas.php?";

        HttpURLConnection urlConnection;

        try {
            //Construir URI
            URL destino = new URL(direccion);
            Log.d("MAPA_ids", "URI: " + destino);

            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200){

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Convertir la respuesta JSON a un array de IDs
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    // Obtener el valor como cadena de texto del JSON Array
                    String stringValue = jsonArray.getString(i);
                    // Convertir la cadena de texto a entero
                    int valor = Integer.parseInt(stringValue);
                    listaIdRandom.add(valor);
                }

                Random random = new Random();
                int id = listaIdRandom.get(random.nextInt(listaIdRandom.size()));
                Log.d("MAPAS_ids",String.valueOf(id));

                Data datosIdRandom = new Data.Builder()
                        .putInt("idRand",id)
                        .build();

                return Result.success(datosIdRandom);

            } else {
                return Result.failure();
            }

        }catch (Exception e){
            Log.d("MAPA","excepcion lanzada");
            return Result.failure();
        }

    }

}
