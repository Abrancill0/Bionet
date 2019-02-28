package com.Danthop.bionet.model;

public class ClienteModel {

    private String cliente_Nombre;
    private String cliente_Correo;
    private String cliente_Telefono;
    private String cliente_direccion;
    private String cliente_ConsumoP;
    private String cliente_Id_Referencia;

    public ClienteModel(String cliente_Nombre, String cliente_Correo, String cliente_Telefono, String cliente_direccion, String cliente_ConsumoP, String cliente_Id_Referencia) {
        this.cliente_Nombre = cliente_Nombre;
        this.cliente_Correo = cliente_Correo;
        this.cliente_Telefono = cliente_Telefono;
        this.cliente_direccion = cliente_direccion;
        this.cliente_ConsumoP = cliente_ConsumoP;
        this.cliente_Id_Referencia = cliente_Id_Referencia;
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

    public String getCliente_direccion() {
        return cliente_direccion;
    }

    public String getCliente_ConsumoP() {
        return cliente_ConsumoP;
    }

    public String getCliente_Id_Referencia() {
        return cliente_Id_Referencia;
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

    public void setCliente_direccion(String cliente_direccion) {
        this.cliente_direccion = cliente_direccion;
    }

    public void setCliente_ConsumoP(String cliente_ConsumoP) {
        this.cliente_ConsumoP = cliente_ConsumoP;
    }

    public void setCliente_Id_Referencia(String cliente_Id_Referencia) {
        this.cliente_Id_Referencia = cliente_Id_Referencia;
    }
}
