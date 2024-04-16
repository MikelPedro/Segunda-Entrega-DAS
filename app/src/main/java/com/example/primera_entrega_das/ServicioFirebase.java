package com.example.primera_entrega_das;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFirebase  extends FirebaseMessagingService {

    private final String CHANNEL_ID = "channelID2"; // Un identificador único para la notificación
    private final String channelName = "channelName2";
    private final int notificacion_id = 0;
    public ServicioFirebase() {
    }

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializar el Handler en el hilo principal de la UI
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if (message.getData().size() > 0) {
            Log.d("FIREBASE_SERVICIO", "hay datos");
        }
        if (message.getNotification() != null) {
            Log.d("FIREBASE_SERVICIO", "hay noti");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Juega", Toast.LENGTH_SHORT).show();
                }
            });

            /*
            String usuario = message.getData().get("usuario");
            // Crea una notificación con 2 opciones
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_fiber_manual_record_24)
                    .setContentTitle("Aviso Preguntas")
                    .setContentText("Hola! " + usuario + " quiere que juegues una partida a Preguntas.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
                     // Acción para compartir con otras apps


            NotificationManagerCompat notificacionman = NotificationManagerCompat.from(this);

            //Se tiene que verificar primero los permisos en la app para lanzar notificaciones
            //esta condicion la incluye el propio android studio para verificar los permisos
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //mostrar y crear notificacion
            notificacionman.notify(notificacion_id, builder.build());
            */

        }


    }


    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("TOKEN", "Refreshed token: " + token);
        super.onNewToken(token);
    }

    /*
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            requestPermissions(new String[] {android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            int importancia = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importancia);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    */
}
