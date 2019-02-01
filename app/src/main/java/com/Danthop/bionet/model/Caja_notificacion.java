package com.Danthop.bionet.model;

public class Caja_notificacion {
    private String Fecha;
    private String Hora;
    private String TextNotificacion;

    public Caja_notificacion(String fecha, String hora, String textNotificacion) {
        Fecha = fecha;
        Hora = hora;
        TextNotificacion = textNotificacion;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getHora() {
        return Hora;
    }

    public String getTextNotificacion() {
        return TextNotificacion;
    }
}
