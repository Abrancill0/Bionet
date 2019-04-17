package com.Danthop.bionet.model;

public class PagoModel {
    private String id;
    private String cantidad;
    private String Nombre;

    public PagoModel(String nombre,String id, String cantidad) {
        this.Nombre = nombre;
        this.id = id;
        this.cantidad = cantidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }
}
