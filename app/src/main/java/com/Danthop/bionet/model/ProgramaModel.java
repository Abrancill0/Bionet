package com.Danthop.bionet.model;

public class ProgramaModel {
    private String Nivel;
    private String PuntosMinimos;
    private String DineroPorPuntos;
    private String PuntosPorDinero;

    public ProgramaModel(String nivel, String puntosMinimos, String dineroPorPuntos, String puntosPorDinero) {
        Nivel = nivel;
        PuntosMinimos = puntosMinimos;
        DineroPorPuntos = dineroPorPuntos;
        PuntosPorDinero = puntosPorDinero;
    }

    public String getNivel() {
        return Nivel;
    }

    public void setNivel(String nivel) {
        Nivel = nivel;
    }

    public String getPuntosMinimos() {
        return PuntosMinimos;
    }

    public void setPuntosMinimos(String puntosMinimos) {
        PuntosMinimos = puntosMinimos;
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
