package com.example.primera_entrega_das;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class DialogoFB extends DialogFragment {
    private String nomUsuario,nomEnviar="";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        //se añade en el builder el tema para el dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setTitle("Enviar mensaje");
        builder.setMessage("Puedes avisar a otro usuario para que juegue hoy! " +
                "o incluso a todos los usuarios dando click a Enviar a Todos");

        // Crear un EditText para que el usuario ingrese el nombre del destinatario
        final EditText nombreUsuarioEditText = new EditText(getActivity());
        nombreUsuarioEditText.setHint("Nombre del usuario");

        // Añadir el EditText al layout del diálogo
        builder.setView(nombreUsuarioEditText);

        //Opcion que se muestra en el dialogo, para enviar mensjae a un usuario
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Obtener nombre de usuario que esta usando la app
                SharedPreferences preferences;
                preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                nomUsuario = preferences.getString("nombreUsu", "");
                Log.d("USUARIO",nomUsuario);
                // Obtener el texto ingresado por el usuario
                nomEnviar = nombreUsuarioEditText.getText().toString().trim();
                Log.d("USUARIO","nom a enviar " + nomEnviar);
                if (!nomEnviar.equals("")){
                    Data datos = new Data.Builder()
                            .putString("nom",nomEnviar)
                            .putString("reg","mensaje")
                            .build();

                    //One time work, llama al dowork que esta en la clase de la conexion con la BD remota
                    OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionBDRemota.class)
                            .setInputData(datos)
                            .build();

                    WorkManager.getInstance(getContext().getApplicationContext()).enqueue(otwr);
                }

            }
        });
        builder.setNeutralButton("Enviar a Todos", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Para enviar a todos los usuarios una notificacion
                Data datos = new Data.Builder()
                        .putString("reg","mensajetodos")
                        .build();

                //One time work, llama al dowork que esta en la clase de la conexion con la BD remota
                OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConexionBDRemota.class)
                        .setInputData(datos)
                        .build();

                WorkManager.getInstance(getContext().getApplicationContext()).enqueue(otwr);
            }
        });
        builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return builder.create();
    }

}
