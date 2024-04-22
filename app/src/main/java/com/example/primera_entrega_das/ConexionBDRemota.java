package com.example.primera_entrega_das;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionBDRemota extends Worker {

    public ConexionBDRemota(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
        }else if(reg.equals("subirtoken")){
            String token = getInputData().getString("token");
            return subirToken(nomUsu,token);
        }else if(reg.equals("mensaje")){
            Log.d("TOKEN_MEN", "llega al doWork");
            return mandarMensajeFB(nomUsu);
        }else if(reg.equals("mensajetodos")){
            Log.d("TOKEN_MEN", "llega al doWork de todos");
            return mandarMensajeTodos();
        }else if(reg.equals("obtpts")){
            return obtPuntos(nomUsu);
        }else if(reg.equals("actpts")) {
            int puntos = getInputData().getInt("ptsBD",0);
            return actPuntos(nomUsu,puntos);
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

        HttpURLConnection urlConnection;
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

        HttpURLConnection urlConnection;
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
            Log.d("IMAGEN", "Codigo de estado del obtener: " + statusCode);

            if(statusCode == 200) {
                try {
                    elBitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());

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

        HttpURLConnection urlConnection;

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
            Log.d("IMAGEN", "Codigo de estado del subir: " + statusCode);
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

    //Metodo para guardar Token de un usuario en la base de datos
    private Result subirToken(String nomUsu, String token) {
        // Construir la URL: IP + PUERTO para el PHP de login
        String direccion = "http://35.230.19.155:81/insertartoken.php?";

        HttpURLConnection urlConnection;

        try {
            //Construir URI con los parametros
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("nombreUsuario", nomUsu)
                    .appendQueryParameter("token", token);
            String parametrosURL = builder.build().getEncodedQuery();

            URL destino = new URL(direccion + parametrosURL);
            Log.d("TOKEN_BD", "URI: " + destino);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int statusCode = urlConnection.getResponseCode();
            Log.d("TOKEN_BD", "Codigo de estado del subir: " + statusCode);
            if(statusCode == 200){
                return Result.success();
            }else{
                return Result.failure();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TOKEN_BD", "EXCEPCION");
            return Result.failure();
        }

    }

    private Result mandarMensajeFB(String nomUsu) {
        // Construir la URL: IP + PUERTO para el PHP de login
        String direccion = "http://35.230.19.155:81/mandarmensaje.php?";
        HttpURLConnection urlConnection;

        try {
            //Construir URI con los parametros (Si se usa Uri.builder poner GET en php)
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("nombreUsuario", nomUsu);
            String parametrosURL = builder.build().getEncodedQuery();

            URL destino = new URL(direccion + parametrosURL);
            Log.d("TOKEN_MEN", "URI: " + destino);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int statusCode = urlConnection.getResponseCode();
            Log.d("TOKEN_MEN", "Codigo de estado del subir: " + statusCode);
            if(statusCode == 200){
                return Result.success();
            }else{
                return Result.failure();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TOKEN_MEN", "EXCEPCION");
            return Result.failure();
        }

    }

    //Mandar mensaje a todos los usuarios (Broadcast)
    private Result mandarMensajeTodos(){
        // Construir la URL: IP + PUERTO para el PHP de login
        String direccion = "http://35.230.19.155:81/mensajeatodos.php";
        HttpURLConnection urlConnection;

        try {
            URL destino = new URL(direccion);
            Log.d("mensaje", "URI: " + destino);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int statusCode = urlConnection.getResponseCode();
            Log.d("mensaje", "Codigo de estado del subir: " + statusCode);
            if(statusCode == 200){
                return Result.success();
            }else{
                return Result.failure();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("mensaje", "EXCEPCION");
            return Result.failure();
        }
    }

    //Metodo para actualizar los puntos del usuario
    private Result actPuntos(String nomUsu, int puntos){
        // Construir la URL: IP + PUERTO para el PHP de login
        String direccion = "http://35.230.19.155:81/actualizarpts.php?";
        HttpURLConnection urlConnection;

        try {
            //Construir URI con los parametros
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("usuario", nomUsu)
                    .appendQueryParameter("ptsGlobal", String.valueOf(puntos));
            String parametrosURL = builder.build().getEncodedQuery();

            URL destino = new URL(direccion + parametrosURL);
            Log.d("PTS_BD", "URI: " + destino);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int statusCode = urlConnection.getResponseCode();
            Log.d("PTS_BD", "Codigo de estado del subir: " + statusCode);
            if(statusCode == 200){
                return Result.success();
            }else{
                return Result.failure();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("PTS_BD", "EXCEPCION");
            return Result.failure();
        }
    }

    //Metodo para obtener los puntos que ha ido acumulando el usuario al jugar partidas
    private Result obtPuntos(String nomUsu){
        // Construir la URL: IP + PUERTO para el PHP de login
        String direccion = "http://35.230.19.155:81/obtenerpts.php?";
        HttpURLConnection urlConnection;

        try {
            //Construir URI con los parametros
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("usuario", nomUsu);
            String parametrosURL = builder.build().getEncodedQuery();

            URL destino = new URL(direccion + parametrosURL);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            Log.d("OBTPTS","La URI: " + String.valueOf(destino));
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int statusCode = urlConnection.getResponseCode();
            if(statusCode == 200){

                // Leer la respuesta como una cadena
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                int puntos = Integer.parseInt(response.toString());
                Log.d("OBTPTS","LOS PUNTOS SON: " + String.valueOf(puntos));
                //Crear output
                Data datospts = new Data.Builder()
                        .putInt("pts", puntos)
                        .build();
                return Result.success(datospts);
            }else{
                return Result.failure();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("OBT_PTS_BD", "EXCEPCION");
            return Result.failure();
        }
    }

}
