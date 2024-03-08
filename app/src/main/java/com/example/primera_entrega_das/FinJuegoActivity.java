package com.example.primera_entrega_das;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FinJuegoActivity extends AppCompatActivity {

    private final String CHANNEL_ID = "channelID"; // Un identificador único para la notificación
    private final String channelName = "channelName";
    private final int notificacion_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_juego);

        //Crear canal de notificaciones
        createNotificationChannel();

        TextView tvFinal = findViewById(R.id.tVFinJuego);
        Bundle extras = getIntent().getExtras();
        if(extras!= null){
            String contPA = extras.getString("pregCorrecta");
            String contPM =  extras.getString("pregRespondidas");
            //Mostrar mensaje con el numero de preguntas mostradas y acertadas
            tvFinal.setText(String.format("Resultado del Juego:\nNumero de preguntas respondidas:%s\nNumero de respuestas correctas:%s", contPM, contPA));
        }else{
            tvFinal.setText("Gracias por jugar");
        }

    }

    public void OnClickFin(View v){
        lanzarNotificacion();
        //finsih();
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            requestPermissions(new String[] {android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            int importancia = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importancia);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void lanzarNotificacion() {
        // Crear un Intent para abrir MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Esto asegura que MainActivity se inicie como una nueva tarea

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Crea una notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Gracias por jugar!")
                .setContentText("Vuelve a la pantalla principal y sigue aprendiendo")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.imagen_jugar, "VALE", pendingIntent);

        NotificationManagerCompat notificacionman = NotificationManagerCompat.from(this);

        //MIRAR: (darle permisos a la app??)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificacionman.notify(notificacion_id, builder.build());

    }
}