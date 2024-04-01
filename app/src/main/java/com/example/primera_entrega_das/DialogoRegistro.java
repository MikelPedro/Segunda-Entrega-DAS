package com.example.primera_entrega_das;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoRegistro extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        //se añade en el builder el tema para el dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setTitle("Registro Guardado");
        builder.setMessage("Tu nombre y contraseña se han guardado correctamente en la base de datos. " +
                            "Disfruta del juego y no olvides guardar tus datos para jugar en otra ocasión. ");

        //Opcion que se muestra en el dialogo
        builder.setPositiveButton("Vale", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Intent para volver a la pantalla de inicio
                Intent iVolver = new Intent(getActivity(), InicioAplicacion.class);
                startActivity(iVolver);
                getActivity().finish(); // Cerrar la actividad actual
                dismiss();

            }
        });
        return builder.create();
    }
}
