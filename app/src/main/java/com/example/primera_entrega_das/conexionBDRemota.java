package com.example.primera_entrega_das;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.Data;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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
        }else if(reg.equals("subir")){
            String foto64 = getInputData().getString("imagen");
            return subirImagenPerfilBD(nomUsu,foto64);
        }else if(reg.equals("obtener")){
            Log.d("IMAGEN", "entra en cargar imagen");
            return obtenerImagenPerfil(nomUsu);
        }else{
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
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
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

    private Result obtenerImagenPerfil(String nomUsuario) {
        // Aquí deberías escribir la lógica para obtener la imagen del perfil del usuario desde la base de datos remota
        // y convertirla en una cadena Base64
        // Por ejemplo:
        Log.d("IMAGEN", "antes de meterse dentro de obtenerImagenBD");
        Bitmap imagenBase64 = obtenerImagenBD(nomUsuario);
        Log.d("IMAGEN", String.valueOf(imagenBase64));

        RecogerImagen.getInstance().setFoto(imagenBase64);
        Log.d("IMAGEN", "despues de llamada a recoger");
        // Devolver el resultado con la imagen Base64
        return Result.success();
    }

    private Bitmap obtenerImagenBD(String nomUsuario) {
        // Metodo para obtener la imagen del perfil del usuario desde la base de datos remota y convertirla en una cadena Base64

        // Construir la URL: IP + PUERTO para el PHP de login
        String direccion = "http://35.230.19.155:81/imgcargar.php?";

        HttpURLConnection urlConnection = null;
        int responseCode = 0;
        Bitmap elBitmap = null;

        try {
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("nombre", nomUsuario);
            String parametrosURL = builder.build().getEncodedQuery();

            URL destino = new URL(direccion + parametrosURL);
            Log.d("IMAGEN", "URI: " + destino);

            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");



            int statusCode = urlConnection.getResponseCode();
            Log.d("IMAGEN", "Codigo de estado del obtener: " + String.valueOf(statusCode));

            if(statusCode == 200) {
                try {
                    elBitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    //elBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                    if (elBitmap != null) {
                        Log.d("IMAGEN", "Bitmap decodificado correctamente");
                        return elBitmap;
                    } else {
                        Log.d("IMAGEN", "El Bitmap es nulo");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("IMAGEN", "Error al decodificar el Bitmap: " + e.getMessage());
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d("IMAGEN","EXCEPCION");
            return null; // Devuelve VACIO en caso de error
        }
        return null;
    }


    private Result subirImagenPerfilBD(String nomUsu, String foto64) {
        // Construir la URL: IP + PUERTO para el PHP de login
        String direccion = "http://35.230.19.155:81/imgsubir.php?";

        HttpURLConnection urlConnection = null;

        try {
            //Construir URI con los parametros
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("nombre", nomUsu)
                    .appendQueryParameter("foto64", foto64);
            String parametrosURL = builder.build().getEncodedQuery();

            URL destino = new URL(direccion + parametrosURL);
            Log.d("IMAGEN", "URI: " + destino);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int statusCode = urlConnection.getResponseCode();
            Log.d("IMAGEN", "Codigo de estado del subir: " + String.valueOf(statusCode));
            if(statusCode == 200){
                return Result.success();
            }else{
                return Result.failure();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("IMAGEN", "EXCEPCION");
            return Result.failure();
        }

    }
}
