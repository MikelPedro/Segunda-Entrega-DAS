package com.example.primera_entrega_das;

public class ModeloTemas {

    private int imagenId;
    private String titulo;
    private String descripcion;

    public ModeloTemas(String titulo, String descripcion, int imagenId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenId = imagenId;
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
