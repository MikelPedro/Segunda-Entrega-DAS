package com.example.primera_entrega_das;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFirebase  extends FirebaseMessagingService {


    public ServicioFirebase() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if (message.getData().size() > 0) {
            Log.d("FIREBASE_SERVICIO", "hay datos");
        }
        if (message.getNotification() != null) {
            Log.d("FIREBASE_SERVICIO", "hay noti");
        }


    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("TOKEN", "Refreshed token: " + token);
        super.onNewToken(token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token);
    }

}
