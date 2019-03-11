package com.Danthop.bionet.model;

public class Puntos_acumulados_model {
    private String id_cliente;
    private String nombre;
    private String acumulado;

    public Puntos_acumulados_model(String id_cliente, String nombre, String acumulado) {
        this.id_cliente = id_cliente;
        this.nombre = nombre;
        this.acumulado = acumulado;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAcumulado() {
        return acumulado;
    }

    public void setAcumulado(String acumulado) {
        this.acumulado = acumulado;
    }
}
