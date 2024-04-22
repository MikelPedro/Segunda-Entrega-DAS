package com.example.primera_entrega_das;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class PreguntasWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Obtener una pregunta aleatoria de la tabla de Cuestionario
        String pregunta = obtenerPreguntaAleatoria(context);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.preguntas_widget);
        views.setTextViewText(R.id.appwidget_text, pregunta);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        //Llamar a la clase de la alarma
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Establecer la alarma para actualizar el widget cada 5 minutos
        long intervalMillis = 5 * 60 * 1000; // 5 minutos en milisegundos
        long triggerAtMillis = System.currentTimeMillis() + intervalMillis;
        alarmManager.setRepeating(AlarmManager.RTC, triggerAtMillis, intervalMillis, pendingIntent);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Cancelar la alarma
        alarmManager.cancel(pendingIntent);
    }

    // Método para obtener una pregunta aleatoria de la tabla de Cuestionario
    private static String obtenerPreguntaAleatoria(Context context) {
        // Obtener una pregunta guardada en strings
        String[] preguntas = context.getResources().getStringArray(R.array.preguntas_array);
        Random random = new Random();
        int index = random.nextInt(preguntas.length);
        return preguntas[index];
    }

}