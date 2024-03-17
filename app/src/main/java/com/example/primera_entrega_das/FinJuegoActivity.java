package com.example.primera_entrega_das;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FinJuegoActivity extends AppCompatActivity {

    private final String CHANNEL_ID = "channelID"; // Un identificador único para la notificación
    private final String channelName = "channelName";
    private final int notificacion_id = 0;
    private int contPA,contPM = 0;


    //Parte necesaria para fragments (horizontal y vertical)

    public static class HorizontalFragment extends Fragment {

        public HorizontalFragment() {
            // Constructor vacío
        }

        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_fin_juego_landscape, container, false);
            //devolver el view que se ha inflado
            return v;
        }
    }

    public static class VerticalFragment extends Fragment {

        public VerticalFragment() {
            // Constructor vacío
        }
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_fin_juego_portrait, container, false);
            //devolver el view que se ha inflado
            return v;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_juego);

        //Comprobar orientacion del movil
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Cargar el Fragment en modo horizontal
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.elFragmento, new HorizontalFragment())
                    .commit();
        } else {

            // Cargar el Fragment en modo vertical
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.elFragmento, new VerticalFragment())
                    .commit();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        //obtener el view del fragmento
        View frag = super.getSupportFragmentManager().findFragmentById(R.id.elFragmento).getView();

        //Crear canal de notificaciones
        createNotificationChannel();

        if(frag!=null) {
            TextView tvFinal = frag.findViewById(R.id.tVFinJuego);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                //Obtener los valores que hemos guardado en el intent de JugarActivity
                contPA = extras.getInt("pregCorrecta");
                contPM = extras.getInt("pregRespondida");
                //Mostrar mensaje con el numero de preguntas mostradas y acertadas
                tvFinal.setText(String.format("Resultado del Juego:\nNúmero de preguntas respondidas:%d\n" +
                        "Número de respuestas correctas:%d", contPM, contPA));
            } else {
                tvFinal.setText("Gracias por jugar");
            }
        }

    }

    public void OnClickFin(View v){
        lanzarNotificacion(); //Llama al metodo que crea una notifiacion
    }

    //Crear canal de notificaciones
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            requestPermissions(new String[] {android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            int importancia = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importancia);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void lanzarNotificacion() {

        //Crear intent para abrir compartir el resultado en la app que quieras (intent implicito)
        Intent intentMensaje = new Intent(Intent.ACTION_SEND);
        intentMensaje.setType("text/plain");
        intentMensaje.putExtra(Intent.EXTRA_TEXT, "Mira mi resultado en la app Preguntas:\n" +
                String.format("Resultado del Juego:\nNúmero de preguntas respondidas:%s\nNúmero de respuestas correctas:%s", contPM, contPA));

        //crear pending intent en el que se incluye el intent con la opcion de compartir el resultado obtenido
        PendingIntent pendingIntentMensaje = PendingIntent.getActivity(this, 0, intentMensaje, PendingIntent.FLAG_IMMUTABLE);

        // Crear un Intent para abrir MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Esto asegura que MainActivity se inicie como una nueva tarea

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Crea una notificación con 2 opciones
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_instrucciones)
                .setContentTitle("Gracias por jugar!")
                .setContentText("Vuelve a la pantalla principal y sigue aprendiendo")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.imagen_jugar, "VALE", pendingIntent)
                .addAction(android.R.drawable.ic_menu_share, "Compartir", pendingIntentMensaje);  // Acción para compartir con otras apps


        NotificationManagerCompat notificacionman = NotificationManagerCompat.from(this);

        //Se tiene que verificar primero los permisos en la app para lanzar notificaciones
        //esta condicion la incluye el propio android studio para verificar los permisos
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //mostrar y crear notificacion
        notificacionman.notify(notificacion_id, builder.build());

    }
}