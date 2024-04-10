package com.example.primera_entrega_das;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class conexionBDRemota extends Worker {


    public conexionBDRemota(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String nomUsu= getInputData().getString("nom");
        String contraseña= getInputData().getString("cont");
        return registro(nomUsu,contraseña);

    }

    public String login(String nomUsu, String contraseña) {
        // método de login aquí
        // Construir la URL para el PHP de login
        String direccion = "http://35.230.19.155/login.php";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(direccion);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            // Leer la respuesta del servidor
            StringBuilder response = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // Cerrar conexiones
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Result registro(String nomUsu, String contraseña) {
        // método de registro aquí
        // Construir la URL para el PHP de registro
        String direccion = "http://35.230.19.155:81/registro.php";
        String parametros = "nombre="+nomUsu+"&contraseña="+contraseña;

        HttpURLConnection urlConnection = null;
        try {
            //Conexion a la URI
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            Log.d("REGISTRO","ENTRA");
            //POST
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //Añadir parametros a la URI
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            int statusCode = urlConnection.getResponseCode();
            Log.d("REGISTRO",String.valueOf(statusCode));
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            Log.d("REGISTRO","EXCEPCION");
            return Result.failure(); // Devuelve null en caso de error
        }

    }

}
