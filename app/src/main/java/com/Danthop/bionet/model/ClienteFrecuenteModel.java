package com.Danthop.bionet.model;

public class ClienteFrecuenteModel {

    private String nombre_cliente;

    public ClienteFrecuenteModel(String nombre_cliente)
    {
        this.nombre_cliente = nombre_cliente;
    }

    public String getnombre_cliente() {return nombre_cliente; }
    public void setnombre_cliente(String nombre_cliente) { this.nombre_cliente = nombre_cliente; }


}
