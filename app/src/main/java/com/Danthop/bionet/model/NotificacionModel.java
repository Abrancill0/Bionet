package com.Danthop.bionet.model;

public class NotificacionModel {
    private String Titulo;
    private String ID;
    private String Fecha;
    private String Mensaje;

    public NotificacionModel(String titulo, String ID, String fecha, String mensaje) {
        Titulo = titulo;
        this.ID = ID;
        Fecha = fecha;
        Mensaje = mensaje;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }
}
