package com.example.primera_entrega_das;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.preguntas_widget);

        // Obtener una pregunta aleatoria de strings.xml
        String pregunta = obtenerPreguntaAleatoria(context);
        remoteViews.setTextViewText(R.id.appwidget_text, pregunta);

        ComponentName widgetComponent = new ComponentName(context, PreguntasWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(widgetComponent, remoteViews);

    }

    private String obtenerPreguntaAleatoria(Context context) {
        String[] preguntasArray = context.getResources().getStringArray(R.array.preguntas_array);
        Random random = new Random();
        int index = random.nextInt(preguntasArray.length);
        return preguntasArray[index];
    }

}
