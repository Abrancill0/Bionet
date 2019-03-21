package com.Danthop.bionet.model;

public class Puntos_acumulados_model {
    private String Numero_cliente;
    private String Nombre;
    private String Correo;
    private String Acumulado;

    public Puntos_acumulados_model(String numero_cliente, String nombre,String correo, String acumulado) {
        this.Numero_cliente = numero_cliente;
        this.Nombre = nombre;
        this.Correo = correo;
        this.Acumulado = acumulado;
    }

    public String getNumero_cliente() {
        return Numero_cliente;
    }

    public void setNumero_cliente(String numero_cliente) {
        this.Numero_cliente = numero_cliente;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getCorreo_cliente() {
        return Correo;
    }

    public void setCorreo_cliente(String correo) {
        this.Correo = correo;
    }


    public String getAcumulado() {
        return Acumulado;
    }

    public void setAcumulado(String acumulado) {
        this.Acumulado = acumulado;
    }
}
