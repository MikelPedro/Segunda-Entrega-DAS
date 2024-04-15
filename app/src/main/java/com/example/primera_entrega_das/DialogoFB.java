package com.example.primera_entrega_das;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

public class DialogoFB extends DialogFragment {
    private String nomUsuario,nomEnviar="";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        //se añade en el builder el tema para el dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setTitle("Enviar mensaje a otro Usuario");
        builder.setMessage("Si quieres puedes avisar a otro usuario para que juegue hoy!");

        // Crear un EditText para que el usuario ingrese el nombre del destinatario
        final EditText nombreUsuarioEditText = new EditText(getActivity());
        nombreUsuarioEditText.setHint("Nombre del usuario");

        // Añadir el EditText al layout del dialogo
        builder.setView(nombreUsuarioEditText);

        //Opcion que se muestra en el dialogo
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
                    enviarMensajeAlUsuario(nomEnviar);
                }

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


    private void enviarMensajeAlUsuario(String nombreUsuario) {
        // Aquí puedes implementar la lógica para enviar el mensaje al usuario.

    }

}
