package com.example.primera_entrega_das;

public class CiudadPregunta {

    private final int codigo;
    private final double latitud;
    private final double longitud;
    private final String ciudad1;
    private final String ciudad2;
    private final String ciudad3;
    private final String ciudadCorrecta;

    public CiudadPregunta(int codigo, double latitud, double longitud, String ciudad1, String ciudad2, String ciudad3, String ciudadCorrecta) {
        this.codigo = codigo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ciudad1 = ciudad1;
        this.ciudad2 = ciudad2;
        this.ciudad3 = ciudad3;
        this.ciudadCorrecta = ciudadCorrecta;
    }

    public int getCodigo() {
        return codigo;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getCiudad1() {
        return ciudad1;
    }

    public String getCiudad2() {
        return ciudad2;
    }

    public String getCiudad3() {
        return ciudad3;
    }

    public String getCiudadCorrecta() {
        return ciudadCorrecta;
    }
}
