package com.example.primera_entrega_das;

public class ModeloTemas {

    private int imagenId;
    private String titulo;
    private String descripcion;

    public ModeloTemas(int imagenId, String titulo) {
        this.imagenId = imagenId;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }


    public int getImagenId() {
        return imagenId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
