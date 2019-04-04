package com.Danthop.bionet.model;

public class ArticuloModel {
    private String articulo_UUID;
    private String articulo_Nombre;
    private String articulo_Descripcion;
    private String articulo_Precio;
    private String articulo_Imagen1;
    private String articulo_Imagen2;
    private String articulo_sku;
    private String articulo_categoria;


    public ArticuloModel(String articulo_UUID,String articulo_Nombre,String articulo_Descripcion ,String articulo_Precio,String articulo_Imagen1,
                         String articulo_Imagen2, String articulo_SKU, String articulo_Categoria) {
        this.articulo_UUID = articulo_UUID;
        this.articulo_Nombre = articulo_Nombre;
        this.articulo_Descripcion = articulo_Descripcion;
        this.articulo_Precio = articulo_Precio;

        this.articulo_Imagen1 = articulo_Imagen1;
        this.articulo_Imagen2 = articulo_Imagen2;
        this.articulo_sku = articulo_SKU;
        this.articulo_categoria = articulo_Categoria;
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

    public String getArticulo_Imagen1() {
        return articulo_Imagen1;
    }

    public String getArticulo_Imagen2() {
        return articulo_Imagen2;
    }


    public void setArticulo_Imagen1(String articulo_Imagen1) {
        this.articulo_Imagen1 = articulo_Imagen1;
    }

    public void setArticulo_Imagen2(String articulo_Imagen2) {
        this.articulo_Imagen2 = articulo_Imagen2;
    }

    public String getArticulo_sku() {
        return articulo_sku;
    }

    public void setArticulo_sku(String articulo_sku) {
        this.articulo_sku = articulo_sku;
    }

    public String getArticulo_categoria() {
        return articulo_categoria;
    }

    public void setArticulo_categoria(String articulo_categoria) {
        this.articulo_categoria = articulo_categoria;
    }
}