package com.Danthop.bionet.model;

public class ArticuloModel {
    private String articulo_UUID;
    private String articulo_Nombre;
    private String articulo_Descripcion;
    private String articulo_Precio;

    public ArticuloModel(String articulo_UUID,String articulo_Nombre,String articulo_Descripcion ,String articulo_Precio) {
        this.articulo_UUID = articulo_UUID;
        this.articulo_Nombre = articulo_Nombre;
        this.articulo_Descripcion = articulo_Descripcion;
        this.articulo_Precio = articulo_Precio;
    }

    public String getarticulo_UUID() {
        return articulo_UUID;
    }

    public String getarticulo_Nombre() {
        return articulo_Nombre;
    }

    public String getarticulo_Descripcion() {
        return articulo_Descripcion;
    }

    public String getarticulo_Precio() {
        return articulo_Precio;
    }

    public void setarticulo_UUID(String articulo_UUID) {
        this.articulo_Precio = articulo_UUID;
    }

    public void setarticulo_Nombre(String articulo_Nombre) {
        this.articulo_Precio = articulo_Nombre;
    }

    public void setarticulo_Descripcion(String articulo_Descripcion) {
        this.articulo_Descripcion = articulo_Descripcion;
    }

    public void setarticulo_Precio(String articulo_Precio) {
        this.articulo_Precio = articulo_Precio;
    }

}