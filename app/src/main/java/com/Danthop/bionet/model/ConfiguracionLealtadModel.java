package com.Danthop.bionet.model;

public class ConfiguracionLealtadModel {
    private String Nivel;
    private String DineroPorPuntos;
    private String PuntosPorDinero;

    public ConfiguracionLealtadModel(String nivel, String dineroPorPuntos, String puntosPorDinero) {
        Nivel = nivel;
        DineroPorPuntos = dineroPorPuntos;
        PuntosPorDinero = puntosPorDinero;
    }

    public String getNivel() {
        return Nivel;
    }

    public void setNivel(String nivel) {
        Nivel = nivel;
    }



    public String getDineroPorPuntos() {
        return DineroPorPuntos;
    }

    public void setDineroPorPuntos(String dineroPorPuntos) {
        DineroPorPuntos = dineroPorPuntos;
    }

    public String getPuntosPorDinero() {
        return PuntosPorDinero;
    }

    public void setPuntosPorDinero(String puntosPorDinero) {
        PuntosPorDinero = puntosPorDinero;
    }
}
