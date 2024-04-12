package com.example.primera_entrega_das;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONObject;

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
        String reg = getInputData().getString("reg");
        if (reg.equals("si")){
            return registro(nomUsu,contraseña);
        }else{
            Log.d("LOGIN", "llega al else");
            return login(nomUsu,contraseña);
        }

    }

    public Result login(String nomUsu, String contraseña) {

        // Construir la URL: IP + PUERTO para el PHP de login
        String direccion = "http://35.230.19.155:81/login.php";
        String parametros = "nombre="+nomUsu+"&contraseña="+contraseña;

        HttpURLConnection urlConnection = null;

        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //Añadir parametros a la URI
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            int statusCode = urlConnection.getResponseCode();


            Log.d("LOGIN", String.valueOf(statusCode));
            // Leer la respuesta del servidor
            return Result.success();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("LOGIN","EXCEPCION");
            return Result.failure(); // Devuelve null en caso de error
        }
    }

    public Result registro(String nomUsu, String contraseña) {
        // método de registro aquí
        // Construir la URL: IP + PUERTO para el PHP de registro
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
