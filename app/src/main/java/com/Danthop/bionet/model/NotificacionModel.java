package com.Danthop.bionet.model;

public class NotificacionModel {
    private String Titulo;
    private String ID;
    private String Fecha;
    private String Mensaje;
    private String Visto;
    private Boolean seleccionado = false;

    public NotificacionModel(String titulo, String ID, String fecha, String mensaje,String visto) {
        Titulo = titulo;
        this.ID = ID;
        Fecha = fecha;
        Mensaje = mensaje;
        Visto = visto;
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

    public String getVisto() {
        return Visto;
    }

    public void setVisto(String visto) {
        Visto = visto;
    }

    public Boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Boolean seleccionado) {
        this.seleccionado = seleccionado;
    }
}
