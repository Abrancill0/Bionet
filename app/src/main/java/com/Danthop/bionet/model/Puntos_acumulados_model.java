package com.Danthop.bionet.model;

public class Puntos_acumulados_model {
    private String Numero_cliente;
    private String Nombre;
    private String Correo;
    private String telefono;
    private String direccion;
    private String rfc;
    private String razon_social;
    private String direccion_fiscal;
    private String Acumulado;

    public Puntos_acumulados_model(String numero_cliente, String nombre,String correo,String telefono, String direccion,
                                   String rfc, String razon_social, String direccion_fiscal, String acumulado) {
        this.Numero_cliente = numero_cliente;
        this.Nombre = nombre;
        this.Correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rfc = rfc;
        this.razon_social = razon_social;
        this.direccion_fiscal = direccion_fiscal;
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

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getrfc() {
        return rfc;
    }
    public void setrfc(String rfc) {
        this.rfc = rfc;
    }

    public String getRazon_social() { return razon_social; }
    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getDireccion_fiscal() {
        return direccion_fiscal;
    }
    public void setDireccion_fiscal(String direccion_fiscal) { this.direccion_fiscal = direccion_fiscal; }

    public String getAcumulado() {
        return Acumulado;
    }
    public void setAcumulado(String acumulado) {
        this.Acumulado = acumulado;
    }
}
