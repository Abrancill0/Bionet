package com.Danthop.bionet.model;

public class ClienteModel {

    private String cliente_Nombre;
    private String cliente_Correo;
    private String cliente_Telefono;
    private String cliente_Ultima_Visita;

    public ClienteModel(String cliente_Nombre, String cliente_Correo, String cliente_Telefono, String cliente_Ultima_Visita) {
        this.cliente_Nombre = cliente_Nombre;
        this.cliente_Correo = cliente_Correo;
        this.cliente_Telefono = cliente_Telefono;
        this.cliente_Ultima_Visita = cliente_Ultima_Visita;
    }

    public String getCliente_Nombre() {
        return cliente_Nombre;
    }

    public String getCliente_Correo() {
        return cliente_Correo;
    }

    public String getCliente_Telefono() {
        return cliente_Telefono;
    }

    public String getCliente_Ultima_Visita() {
        return cliente_Ultima_Visita;
    }

    public void setCliente_Nombre(String cliente_Nombre) {
        this.cliente_Nombre = cliente_Nombre;
    }

    public void setCliente_Correo(String cliente_Correo) {
        this.cliente_Correo = cliente_Correo;
    }

    public void setCliente_Telefono(String cliente_Telefono) {
        this.cliente_Telefono = cliente_Telefono;
    }

    public void setCliente_Ultima_Visita(String cliente_Ultima_Visita) {
        this.cliente_Ultima_Visita = cliente_Ultima_Visita;
    }
}
