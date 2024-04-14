package com.example.primera_entrega_das;

import android.graphics.Bitmap;

public class RecogerImagen {

    private static RecogerImagen imagen;

    private Bitmap foto;

    private RecogerImagen() {
    }

    public static RecogerImagen getInstance(){
        if (RecogerImagen.imagen == null){
            RecogerImagen.imagen = new RecogerImagen();
        }
        return RecogerImagen.imagen;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}
