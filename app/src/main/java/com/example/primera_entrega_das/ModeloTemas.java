package com.example.primera_entrega_das;

public class ModeloTemas {

    //Clase para guardar todos los atributos relacionados con el tema en una clase llamada Tema
    private final int imagenId;
    private final String titulo;
    private final String descripcion;

    public ModeloTemas(String titulo, String descripcion, int imagenId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenId = imagenId;
    }

    //Getters
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
