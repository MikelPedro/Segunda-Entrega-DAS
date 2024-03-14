package com.example.primera_entrega_das;

public class ModeloPregunta {

    //Clase para guardar todos los atributos relacionados con la pregunta en una clase llamada ModeloPregunta
    private final int codigo;
    private final String pregunta;
    private final String respuesta1;
    private final String respuesta2;
    private final String respuesta3;
    private final String respuestaCorrecta;

    public ModeloPregunta(int codigo, String pregunta, String respuesta1, String respuesta2, String respuesta3, String respuestaCorrecta) {
        this.codigo = codigo;
        this.pregunta = pregunta;
        this.respuesta1 = respuesta1;
        this.respuesta2 = respuesta2;
        this.respuesta3 = respuesta3;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    //Getters
    public String getPregunta() {
        return pregunta;
    }

    public String getRespuesta1() {
        return respuesta1;
    }

    public String getRespuesta2() {
        return respuesta2;
    }

    public String getRespuesta3() {
        return respuesta3;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }
}
