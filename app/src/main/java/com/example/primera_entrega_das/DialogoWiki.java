package com.example.primera_entrega_das;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoWiki extends DialogFragment {
    //hola
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        builder.setTitle("¿Cómo jugar y añadir preguntas?");
        builder.setMessage("Puedes jugar contestando a las preguntas que vienen por defecto de manera aleatoria " +
                        "o por temas, además puedes crear más preguntas. La información para crear preguntas se puede encontrar" +
                        " en páginas como Wikipedia.\n");

        //Opcion para ver enlace que lleva a la pagina de wikipedia en internet mediante un intent implicito
        builder.setPositiveButton("Ver enlace", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Intent para abrir Wikipedia en el navegador
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://es.wikipedia.org"));
                startActivity(intent);
                dismiss();
            }
        });

        //Opcion para quitar el dialogo
        builder.setNegativeButton("Volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return builder.create();
    }
}
