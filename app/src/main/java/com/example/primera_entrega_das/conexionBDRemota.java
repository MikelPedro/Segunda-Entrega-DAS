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
import java.net.HttpURLConnection;
import java.net.URL;

public class conexionBDRemota extends Worker {


    public conexionBDRemota(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }

    public String login(String nomUsu, String contraseña) {
        // método de login aquí
        // Construir la URL para el PHP de login
        String direccion = "http://35.230.19.155/login.php?nombre_usuario=" + nomUsu + "&contraseña=" + contraseña;

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

    public void registro(String nomUsu, String contraseña) {
        // método de registro aquí
        // Construir la URL para el PHP de registro
        String direccion = "http://35.230.19.155/registro.php?nombre_usuario=" + nomUsu + "&contraseña=" + contraseña;




    }

}
